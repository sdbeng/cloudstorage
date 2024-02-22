package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialsService {

    private CredentialsMapper credentialsMapper;
    private EncryptionService encryptionService;

    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    //getCredentials
    public List<Credential> getCredentials(Integer userId) {
        List<Credential> credentialsList = credentialsMapper.getCredentials(userId);
        credentialsList.stream().forEach(cr -> cr.setDecryptedPassword(encryptionService.decryptValue(cr.getPassword(), cr.getKey())));
        return credentialsList;
    }

    //addCredentials
    public int addCredentials(User user, CredentialForm credentialForm) {
        System.out.println("LOG-service credentialForm===" + credentialForm);
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);
        return credentialsMapper.insertCredentials(new Credential(null, credentialForm.getUrl(), credentialForm.getUsername(), encodedKey, encryptedPassword, user.getUserId()));
    }
    //editCredentials
    public int editCredentials(CredentialForm credentialForm) {
        System.out.println("LOG-updating credential...");
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);
        return credentialsMapper.updateCredentials(new Credential(credentialForm.getCredentialId(), credentialForm.getUrl(), credentialForm.getUsername(), encodedKey, encryptedPassword, null));
    }

    //deleteCredentials
    public int deleteCredentials(Integer credentialId) {
        return credentialsMapper.deleteCredentials(credentialId);
    }


    public Credential getCredential(int credentialId) {
        return credentialsMapper.getCredentialById(credentialId);
    }
}
