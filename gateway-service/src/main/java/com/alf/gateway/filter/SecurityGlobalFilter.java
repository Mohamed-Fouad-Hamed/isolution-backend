package com.alf.gateway.filter;

import com.alf.gateway.config.SecurityProperties;
import com.alf.gateway.jwt.JwtVerifierService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.List;

@Component
public class SecurityGlobalFilter implements GlobalFilter, Ordered {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final JwtVerifierService verifier;
    private final SecurityProperties props;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public SecurityGlobalFilter(ReactiveStringRedisTemplate redisTemplate, JwtVerifierService verifier, SecurityProperties props) {
        this.redisTemplate = redisTemplate;
        this.verifier = verifier;
        this.props = props;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        String path = exchange.getRequest().getURI().getPath();

        List<String> excluded = props.getFilterExcludeUrls();
        if (excluded != null) {
            for (String pattern : excluded) {
                if (pathMatcher.match(pattern, path)) {
                    return chain.filter(exchange);
                }
            }
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, HttpStatus.UNAUTHORIZED, "MISSING_TOKEN", "Please provide a valid token");
        }

        String token = authHeader.substring(7);

        try {

            var claims = verifier.getClaimsSet(token);

            String jti = claims.getJWTID();

            String userId = claims.getSubject();


            // 2. فحص الجلسة الواحدة في Redis
            return redisTemplate.opsForValue().get("user_session:" + userId)
                    .flatMap(activeJti -> {
                        if (activeJti != null && !activeJti.equals(jti)) {
                            return onError(exchange, HttpStatus.UNAUTHORIZED, "SESSION_REPLACED", "Logged in from another device");
                        }
                        // تمرير بيانات المستخدم للخدمات الخلفية في Headers لتقليل الضغط

                       ServerHttpRequest mutatedRequest = null;
                        try {
                            mutatedRequest = exchange.getRequest().mutate()
                                    .header("X-User-Id", userId)
                                    .header("X-User-Roles",  claims.getStringClaim("roles"))
                                    .build();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());

                       // return chain.filter(exchange);
                    })
                    .switchIfEmpty(chain.filter(exchange));

        } catch (ExpiredJwtException e) {
            return onError(exchange, HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "Token has expired");
        } catch (Exception e) {
            return onError(exchange, HttpStatus.FORBIDDEN, "INVALID_TOKEN", "Security validation failed");
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status, String errCode, String errMsg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("X-Custom-Error", errCode);
        response.getHeaders().add("X-Error-Message", errMsg);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0; // بعد الـ Rate Limiter (-1) وقبل الـ Routing
    }
}