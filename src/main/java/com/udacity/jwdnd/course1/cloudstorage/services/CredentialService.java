package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService){

        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int saveCredential(CredentialForm credentialForm, int userid) throws IOException {
        String url = credentialForm.getUrl();
        String username = credentialForm.getUsername();
        String password = credentialForm.getPassword();
        Integer credentialId = credentialForm.getCredentialId();

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        Credential credential= new Credential(credentialId,url,username,encodedKey,encryptedPassword,userid);

        if(credentialId != null){
            return credentialMapper.editCredential(credential);
        }
        return credentialMapper.addCredential(credential);
    }

    public List<Credential> getCredentials(int userid){
        return credentialMapper.getCredentials(userid);
    }

    public int deleteCredential(int credentialid, int userid){

        return credentialMapper.deleteCredential(credentialid,userid);
    }
}
