package edu.tum.ase.casservice.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.EncryptedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Encrypted;
import com.nimbusds.jose.crypto.RSADecrypter;
import org.springframework.stereotype.Component;


import java.security.interfaces.RSAPrivateKey;
import java.text.ParseException;
@Component
public class JweUtil {
    @Autowired
    private KeyStoreManager keyStoreManager;

    public String decryptPasswordInJwe(String password) {
        RSADecrypter decrypter = new RSADecrypter((RSAPrivateKey) keyStoreManager.getPrivateKey());
//        keyStoreManager.getPrivateKey().getPrivateExponent();
        String decryptedPw = "";
        try {
            // TODO: Parse the encrypted JWE
            EncryptedJWT jwt=EncryptedJWT.parse(password);
            // TODO: Decrypt the JWE and extract the "password" info
            jwt.decrypt(decrypter);
            decryptedPw=jwt.getJWTClaimsSet().getClaim("password").toString();

        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
        }
        return decryptedPw;
    }
}
