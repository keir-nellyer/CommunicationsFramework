package com.ikeirnez.communicationsframework.api.config.authentication;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents an authentication method which encrypts a password and is checked server-side.
 */
public class SimpleAuthenticationConfig extends AuthenticationConfig {

    private String encryptedPassword;

    public SimpleAuthenticationConfig(String password){
        try {
            this.encryptedPassword = new String(MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("Error whilst encrypting password for authentication", e);
        }
    }

    public String getEncryptedPassword(){
        return encryptedPassword;
    }

}
