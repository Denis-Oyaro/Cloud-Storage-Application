package com.udacity.jwdnd.course1.cloudstorage.entity;

public class DisplayedCredential {
    private Credential credential;
    private String decryptedPassword;

    public DisplayedCredential(Credential credential, String decryptedPassword) {
        this.credential = credential;
        this.decryptedPassword = decryptedPassword;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public void setDecryptedPassword(String decryptedPassword) {
        this.decryptedPassword = decryptedPassword;
    }

    public Credential getCredential() {
        return credential;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }
}
