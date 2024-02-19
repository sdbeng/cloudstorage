package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialsService {

    private CredentialsMapper credentialsMapper;
    private EncryptionService encryptionService;

    public CredentialsService(CredentialsMapper credentialsMapper) {
        this.credentialsMapper = credentialsMapper;
//        this.encryptionService = encryptionService;
    }

    //getCredentials
    public List<Credentials> getCredentials() {
        return credentialsMapper.getCredentials();
    }
    //get one credential
    public Credentials getCredentialById(int credentialId) {
        return credentialsMapper.getCredentialById(credentialId);
    }
    //addCredentials
    public void addCredentials(Credentials credentials) {
        System.out.println("LOG-Credentials service credentials===" +  credentials);
        //todo: decrypt password before returning to the client
        String encryptedPassword = credentials.getPassword();
        credentials.setPassword(encryptedPassword);
        credentialsMapper.insertCredentials(credentials);
    }
    //editCredentials
    public void editCredentials(Credentials credentials) {
        Credentials storedNewCredentials = credentialsMapper.getCredentialById(credentials.getCredentialId());

        credentials.setKey(storedNewCredentials.getKey());
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), storedNewCredentials.getKey());
        credentials.setPassword(encryptedPassword);
        credentialsMapper.updateCredentials(credentials);
    }

    //deleteCredentials
    public void deleteCredentials(int credentialId) {
        credentialsMapper.deleteCredentials(credentialId);
    }


}
