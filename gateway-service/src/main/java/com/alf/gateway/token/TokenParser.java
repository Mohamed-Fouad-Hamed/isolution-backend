package com.alf.gateway.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

public class TokenParser {

    /**
     * Parses a JWT string into a SignedJWT instance.
     */
    public SignedJWT parse(String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    /**
     * Verifies the signature of the parsed SignedJWT using the provided key.
     * Supports:
     * - RSAPublicKey -> RSASSA verifier
     * - ECPublicKey  -> ECDSA verifier
     * - SecretKey or byte[] -> HMAC verifier (MAC)
     *
     * Returns true if signature verifies, false otherwise.
     */
    public boolean verify(SignedJWT jwt, Key key) throws JOSEException {
        JWSVerifier verifier = createVerifierForKey(key)
                .orElseThrow(() -> new JOSEException("Unsupported key type for verification: " + key.getClass().getName()));

        return jwt.verify(verifier);
    }

    public boolean verify(SignedJWT jwt, byte[] hmacSecret) throws JOSEException {
        JWSVerifier verifier = new MACVerifier(hmacSecret);
        return jwt.verify(verifier);
    }

    private Optional<JWSVerifier> createVerifierForKey(Key key) throws JOSEException {

        if (key instanceof RSAPublicKey) {
            return Optional.of(new RSASSAVerifier((RSAPublicKey) key));
        }

        if (key instanceof ECPublicKey) {
            return Optional.of(new ECDSAVerifier((ECPublicKey) key));
        }

        if (key instanceof SecretKey) {
            // HMAC shared secret
            return Optional.of(new MACVerifier(((SecretKey) key).getEncoded()));
        }

        // unsupported
        return Optional.empty();
    }


    /**
     * Returns the "sub" claim (subject) or null if missing.
     */
    public String getSubject(SignedJWT jwt) throws ParseException {
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        return claims.getSubject();
    }

    /**
     * Returns true if token is expired (expiration time before now).
     * If no expiration claim present, returns false (you can change to true if you prefer strict behavior).
     */
    public boolean isExpired(SignedJWT jwt) throws ParseException {
        Date exp = jwt.getJWTClaimsSet().getExpirationTime();
        if (exp == null) return false;
        return exp.before(new Date());
    }

    /**
     * Returns the JWT ID (jti) claim or null if missing.
     */
    public String getJti(SignedJWT jwt) throws ParseException {
        return jwt.getJWTClaimsSet().getJWTID();
    }

}
