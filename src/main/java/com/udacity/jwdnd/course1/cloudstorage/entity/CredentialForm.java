package com.udacity.jwdnd.course1.cloudstorage.entity;

public class CredentialForm {
    private String url;
    private String username;
    private String password;
    private Integer credentialId;

    public CredentialForm(String url, String username, String password, Integer credentialId) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.credentialId = credentialId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getCredentialId() {
        return credentialId;
    }
}
