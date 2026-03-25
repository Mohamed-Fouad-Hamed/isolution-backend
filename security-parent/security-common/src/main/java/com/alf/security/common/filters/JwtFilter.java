package com.alf.security.common.filters;

import com.alf.security.common.SecurityProperties;
import com.alf.security.common.jwt.JwtVerifierService;
import com.alf.security.common.token.TokenParser;
import com.alf.security.common.jwt.JwtBlacklist;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {
    private static final Logger log =
            LoggerFactory.getLogger(JwtFilter.class);
    private final JwtVerifierService verifier;
    private final JwtBlacklist blacklist;
    private final SecurityProperties props;
    private final TokenParser parser;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtFilter(JwtVerifierService verifier,
                     JwtBlacklist blacklist,
                     TokenParser parser,
                     SecurityProperties props,
                     HandlerExceptionResolver handlerExceptionResolver) {
        this.verifier = verifier;
        this.blacklist = blacklist;
        this.props = props;
        this.parser = parser;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
                                    throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();

        List<String> excluded = props.getFilterExcludeUrls();
        if (excluded != null) {
            for (String pattern : excluded) {
                if (pathMatcher.match(pattern, path)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }


        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(request, response); // no token -> let security handle (may be anonymously allowed)
            return;
        }
        String token = auth.substring(7).trim();
        try {
            var claims = verifier.getClaimsSet(token);
            String jti = claims.getJWTID();
            if (jti != null && blacklist.isBlacklisted(jti)) {
                SecurityContextHolder.clearContext();
                throw new BadCredentialsException("TOKEN_REVOKED");
            }
            // map subject + roles to Authentication
            String subject = claims.getSubject();
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            var roles = claims.getStringClaim("roles");
            if (roles != null) {
                // expect roles as CSV or JSON array string; adapt to your claim format
                for (String r : roles.split(",")) authorities.add(new
                        SimpleGrantedAuthority(r.trim()));
            }
            UsernamePasswordAuthenticationToken authToken = new
                    UsernamePasswordAuthenticationToken(subject, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
            chain.doFilter(request, response);
        } catch (JwtVerifierService.JwtVerificationException e) {
            SecurityContextHolder.clearContext();
            throw new BadCredentialsException("INVALID_TOKEN", e);
        } catch (Exception ex) {
            // This sends the exception to your GlobalExceptionHandler
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();
        List<String> excludedList = props.getFilterExcludeUrls();

        if (excludedList == null || excludedList.isEmpty()) {
            return false;
        }

        boolean excluded = excludedList.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));

        log.info("[JwtAuthFilter] shouldNotFilter({}) = {}", path, excluded);
        return excluded;
    }
}