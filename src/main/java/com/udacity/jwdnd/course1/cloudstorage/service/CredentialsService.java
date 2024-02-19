package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
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
    public List<Credential> getCredentials() {
        return credentialsMapper.getCredentials();
    }
    //get one credential
    public Credential getCredentialById(int credentialId) {
        return credentialsMapper.getCredentialById(credentialId);
    }
    //addCredentials
    public void addCredentials(Credential credential) {
        System.out.println("LOG-Credentials service credentials===" + credential);
        //todo: decrypt password before returning to the client
        String encryptedPassword = credential.getPassword();
        credential.setPassword(encryptedPassword);
        credentialsMapper.insertCredentials(credential);
    }
    //editCredentials
    public void editCredentials(Credential credential) {
        Credential storedNewCredentials = credentialsMapper.getCredentialById(credential.getCredentialId());

        credential.setKey(storedNewCredentials.getKey());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), storedNewCredentials.getKey());
        credential.setPassword(encryptedPassword);
        credentialsMapper.updateCredentials(credential);
    }

    //deleteCredentials
    public void deleteCredentials(int credentialId) {
        credentialsMapper.deleteCredentials(credentialId);
    }


}
