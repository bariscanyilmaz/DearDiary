package com.bariscanyilmaz.deardiary.di;

import java.security.MessageDigest;
import java.util.Base64;

import javax.inject.Inject;

public class HashService {

    @Inject
    public HashService(){

    }

    public String hashPassword(String password){

        MessageDigest digest=null;
        String hash="";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());

            hash = Base64.getEncoder().encodeToString(digest.digest());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return hash;
    }

}
