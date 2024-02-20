package com.udacity.jwdnd.course1.cloudstorage.model;

public class Credential {
    public Integer credentialId;
    public String url;
    public String username;
    public String key;
    public String password;
    public Integer userid;
    private String decryptedPassword;

    public Credential( Integer credentialId, String url, String username, String key, String password, Integer userid ) {
        this.credentialId = credentialId;
        this.url = url;
        this.username = username;
        this.key = key;
        this.password = password;
        this.userid = userid;
    }

    public Integer getCredentialId() {
        return credentialId;
    }

    public void setCredentialId( Integer credentialId ) {
        this.credentialId = credentialId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public String getUserName() {
        System.out.println("getter username===" + username);
        return username;
    }

    public void setUserName( String username ) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey( String key ) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid( Integer userid ) {
        this.userid = userid;
    }

    public void setDecryptedPassword(String decryptedPassword) {
        this.decryptedPassword = decryptedPassword;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }
}
