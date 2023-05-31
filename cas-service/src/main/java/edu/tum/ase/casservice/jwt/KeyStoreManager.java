package edu.tum.ase.casservice.jwt;


import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;

@Component
public class KeyStoreManager {
    private KeyStore keyStore;
    private String keyAlias;
    private char[] password = "123456".toCharArray();

    public KeyStoreManager() throws KeyStoreException, IOException {
        loadKeyStore();
    }

    public void loadKeyStore() throws KeyStoreException, IOException {
        keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fis = null;
        try {
            // Get the path to the keystore file in the resources folder
            File keystoreFile = ResourceUtils.getFile("classpath:ase_project.keystore");    // 文件名就是ase_project
            fis = new FileInputStream(keystoreFile);
            keyStore.load(fis, password);
            keyAlias = keyStore.aliases().nextElement();
        } catch (Exception e) {
            System.err.println("Error when loading KeyStore");
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public PublicKey getPublicKey() {
        try {
            // TODO: return the public key in the keystore
            return keyStore.getCertificate(keyAlias).getPublicKey();
//            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected Key getPrivateKey() {
        try {
            // TODO: return the private key in the keystore.
            return keyStore.getKey(keyAlias, password);
//            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


}