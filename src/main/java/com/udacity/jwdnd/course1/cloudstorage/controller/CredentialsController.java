package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

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

    @PostMapping("/create")
    public String addUpdateCredentials(Authentication authentication, Credentials credentials, Model model) {
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
            System.out.println("Credential id not found, adding credentials...");
            credentials.setUserid(userId);
            credentialsService.addCredentials(credentials);
            successMessage = "Credentials added successfully!";
        } else {
            credentialsService.editCredentials(credentials);
            successMessage = "Credentials updated successfully!";
        }


        List<Credentials> credentialsList = credentialsService.getCredentials();
        System.out.println("===credentialsList=== " + credentialsList);
//        model.addAttribute("credentials", credentialsList);
        return "redirect:/home?success";
    }

    @ModelAttribute("credentials")
    public List<Credentials> getCredentials(){
        List<Credentials> newCredentials = credentialsService.getCredentials();
        for(Credentials c : credentialsService.getCredentials()){
            c.setDecryptedPassword(encryptionService.decryptValue(c.getPassword(), c.getKey()));
        }
        System.out.println("===new credentials=== " + newCredentials);
        return newCredentials;
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
