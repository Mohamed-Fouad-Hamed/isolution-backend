package com.alf.gateway.jwt;


import com.alf.gateway.jwks.JwksCache;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * Service to verify JWT tokens using JWKS fetched by JwksCache.
 * - validates signature algorithm, kid, signature, exp, nbf
 * - supports configurable clock skew
 */
public class JwtVerifierService {
    private static final Logger log =
            LoggerFactory.getLogger(JwtVerifierService.class);
    private final JwksCache jwksCache;
    private final long clockSkewSeconds; // allowed clock skew
    private final Clock clock;
    public JwtVerifierService(JwksCache jwksCache, long clockSkewSeconds) {
        this(jwksCache, clockSkewSeconds, Clock.systemUTC());
    }
    // constructor for tests
    public JwtVerifierService(JwksCache jwksCache, long clockSkewSeconds, Clock
            clock) {
        this.jwksCache = jwksCache;
        this.clockSkewSeconds = Math.max(0, clockSkewSeconds);
        this.clock = clock;
    }

    public JWTClaimsSet getClaimsSet(String token) {
       return getClaims(token);
    }

    public JWTClaimsSet verify(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSHeader header = signedJWT.getHeader();
            if (header.getAlgorithm() == null || !
                    JWSAlgorithm.RS256.equals(header.getAlgorithm())) {
                throw new JwtVerificationException("Unsupported JWS algorithm: " + header.getAlgorithm());
            }
            String kid = header.getKeyID();
            if (kid == null) throw new JwtVerificationException("Missing kid in JWT header");
            // get public key
            RSAKey rsaKey = (RSAKey) jwksCache.get().getKeyByKeyId(kid);
            if (rsaKey == null) throw new JwtVerificationException("No public key found for kid=" + kid);
            RSAPublicKey pub = rsaKey.toRSAPublicKey();
            JWSVerifier verifier = new RSASSAVerifier(pub);
            if (!signedJWT.verify(verifier)) throw new
                    JwtVerificationException("Signature verification failed");
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            // claims time validations with clock skew
            Date now = Date.from(Instant.now(clock));
            Date exp = claims.getExpirationTime();
            Date nbf = claims.getNotBeforeTime();
            if (exp != null &&
                    now.toInstant().isAfter(exp.toInstant().plusSeconds(clockSkewSeconds))) {
                    throw new JwtVerificationException("Token expired");
            }
            if (nbf != null &&
                    now.toInstant().isBefore(nbf.toInstant().minusSeconds(clockSkewSeconds))) {
                    throw new JwtVerificationException("Token is not yet valid (nbf)");
            }
            return claims;
        } catch (ParseException pe) {
            log.debug("Failed to parse JWT", pe);
            throw new JwtVerificationException("Invalid JWT format", pe);
        } catch (JwtVerificationException jve) {
            throw jve;
        } catch (Exception e) {
            log.error("Unexpected error while verifying JWT", e);
            throw new JwtVerificationException("Unexpected verification error", e);
        }
    }

    public boolean validToken(String token) {
        try {

            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSHeader header = signedJWT.getHeader();
            if (header.getAlgorithm() == null || !
                    JWSAlgorithm.RS256.equals(header.getAlgorithm())) {
                throw new JwtVerificationException("Unsupported JWS algorithm: " + header.getAlgorithm());
            }
            String kid = header.getKeyID();
            if (kid == null) throw new JwtVerificationException("Missing kid in JWT header");
            // get public key
            RSAKey rsaKey = (RSAKey) jwksCache.get().getKeyByKeyId(kid);
            if (rsaKey == null) throw new JwtVerificationException("No public key found for kid=" + kid);
            RSAPublicKey pub = rsaKey.toRSAPublicKey();
            JWSVerifier verifier = new RSASSAVerifier(pub);
            if (!signedJWT.verify(verifier)) throw new
                    JwtVerificationException("Signature verification failed");
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            // claims time validations with clock skew
            Date now = Date.from(Instant.now(clock));
            Date exp = claims.getExpirationTime();
            Date nbf = claims.getNotBeforeTime();
            if (exp != null &&
                    now.toInstant().isAfter(exp.toInstant().plusSeconds(clockSkewSeconds))) {
                throw new JwtVerificationException("Token expired");
            }
            if (nbf != null &&
                    now.toInstant().isBefore(nbf.toInstant().minusSeconds(clockSkewSeconds))) {
                throw new JwtVerificationException("Token is not yet valid (nbf)");
            }
            return true;
        } catch (ParseException pe) {
            log.debug("Failed to parse JWT", pe);
            throw new JwtVerificationException("Invalid JWT format", pe);
        } catch (JwtVerificationException jve) {
            throw jve;
        } catch (Exception e) {
            log.error("Unexpected error while verifying JWT", e);
            throw new JwtVerificationException("Unexpected verification error", e);
        }
    }

    private JWTClaimsSet getClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSHeader header = signedJWT.getHeader();
            if (header.getAlgorithm() == null || !
                    JWSAlgorithm.RS256.equals(header.getAlgorithm())) {
                throw new JwtVerificationException("Unsupported JWS algorithm: " + header.getAlgorithm());
            }
            String kid = header.getKeyID();
            if (kid == null) throw new JwtVerificationException("Missing kid in JWT header");
            // get public key
            RSAKey rsaKey = (RSAKey) jwksCache.get().getKeyByKeyId(kid);
            if (rsaKey == null) throw new JwtVerificationException("No public key found for kid=" + kid);
            RSAPublicKey pub = rsaKey.toRSAPublicKey();
            JWSVerifier verifier = new RSASSAVerifier(pub);
            if (!signedJWT.verify(verifier)) throw new
                    JwtVerificationException("Signature verification failed");
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date now = Date.from(Instant.now(clock));
            Date exp = claims.getExpirationTime();
            Date nbf = claims.getNotBeforeTime();
            if (exp != null &&
                    now.toInstant().isAfter(exp.toInstant().plusSeconds(clockSkewSeconds))) {
                throw new JwtVerificationException("Token expired");
            }
            if (nbf != null &&
                    now.toInstant().isBefore(nbf.toInstant().minusSeconds(clockSkewSeconds))) {
                throw new JwtVerificationException("Token is not yet valid (nbf)");
            }
            return claims;
        } catch (ParseException pe) {
            log.debug("Failed to parse JWT", pe);
            throw new JwtVerificationException("Invalid JWT format", pe);
        } catch (JwtVerificationException jve) {
            throw jve;
        } catch (Exception e) {
            log.error("Unexpected error while verifying JWT", e);
            throw new JwtVerificationException("Unexpected verification error", e);
        }

    }

    public String getSubject(String token) {
        JWTClaimsSet claims = getClaims(token);
        return Optional.ofNullable(claims)
                .map(JWTClaimsSet::getSubject)
                .filter(sub -> !sub.isBlank())
                .orElseThrow(() -> new JwtVerificationException("Invalid subject in token"));
    }

    public long getExpirationMillis(String token) {
        JWTClaimsSet claims = getClaims(token);
        return Optional.ofNullable(claims)
                .map(JWTClaimsSet::getExpirationTime)
                .map(Date::toInstant)
                .map(Instant::toEpochMilli)
                .orElseThrow(() -> new JwtVerificationException("Missing exp in token"));
    }

    public static class JwtVerificationException extends RuntimeException {
        public JwtVerificationException(String message) { super(message); }
        public JwtVerificationException(String message, Throwable cause) {
            super(message, cause); }
    }
}
