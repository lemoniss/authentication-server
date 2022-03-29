package com.maxlength.spec.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    public Map<String, Object> decode(String jwt, String publicKey) {

        publicKey = publicKey
            .replaceAll("-----BEGIN PUBLIC KEY-----", "")
            .replaceAll("-----END PUBLIC KEY-----", "")
            .replaceAll("\\r\\n|\\r|\\n", "");
        Map<String, Object> map = new HashMap<>();

        try {
            Claims claims = Jwts
                .parser()
                .setSigningKey(generatePublicKey(publicKey))
                .parseClaimsJws(jwt)
                .getBody();
            map = claims;
        } catch (InvalidKeySpecException ie) {
        } catch (NoSuchAlgorithmException ne) {
        }

        return map;
    }

    public PublicKey generatePublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
        byte[] publicKeyByte = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
