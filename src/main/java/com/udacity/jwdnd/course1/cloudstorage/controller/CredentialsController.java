package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;
import java.util.Base64;

@RequestMapping("/home/credentials")
@Controller
public class CredentialsController {
    private CredentialsService credentialsService;
    private UserMapper userMapper;
    private EncryptionService encryptionService;
        String successMessage = null;

    public CredentialsController(CredentialsService credentialsService, UserMapper userMapper, EncryptionService encryptionService){
        this.credentialsService = credentialsService;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    @PostMapping()
    public String addUpdateCredentials(Authentication authentication, Credentials credentials) {
        String username = (String) authentication.getPrincipal();
        User user = userMapper.getUser(username);
        int userId = user.getUserId();

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), encodedKey);
        credentials.setKey(encodedKey);
        credentials.setPassword(encryptedPassword);

        if(credentials.getCredentialId() == null) {
            System.out.println("Credentials not found, adding credentials...");
            credentials.setUserid(userId);
            credentialsService.addCredentials(credentials);
            successMessage = "Credentials added successfully!";
        } else {
            credentialsService.editCredentials(credentials);
            successMessage = "Credentials updated successfully!";
        }

        return "redirect:/home?success";
    }

    @GetMapping("/delete") //passing the @RequestParam("credentialId") to indicate that the credentialId is passed as a parameter
    public String deleteCredentials(@RequestParam("credentialId") int credentialId) {
        if(credentialId > 0){
            credentialsService.deleteCredentials(credentialId);
            return "redirect:/home?success";
        } else {
            return "redirect:/result?error";
        }
    }

}
