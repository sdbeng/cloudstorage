package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/credentials")
public class CredentialsController {
    private UserService userService;
    private CredentialsService credentialsService;
    private UserMapper userMapper;
    private EncryptionService encryptionService;
        String successMessage = null;

    public CredentialsController(CredentialsService credentialsService, UserMapper userMapper, UserService userService, EncryptionService encryptionService){
        this.userService = userService;
        this.credentialsService = credentialsService;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/add")
    public String addUpdateCredentials(Authentication authentication, CredentialForm credentialForm, Model model) {
        User user = null;
        try {
            user = userService.getUser(authentication.getName());
            System.out.println("user===" + user);
        } catch (Exception e) {
            System.out.println("Error getting user: " + e.getMessage());
            throw new RuntimeException(e);
        }
        if(credentialForm.getCredentialId() == null) {
        if(credentialsService.addCredentials(user, credentialForm) == 1){
            model.addAttribute("success", true);
            model.addAttribute("successMessage", "Credentials added successfully!");
            return "redirect:/home#nav-credential";
//            return "redirect:/result?success";

        }else{
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", "Error adding credentials!");
            System.out.println("Error adding credentials!");
        }
    }else {
        if(credentialsService.editCredentials(credentialForm) == 1){
            model.addAttribute("success", true);
            model.addAttribute("successMessage", "Credentials updated successfully!");
            return "redirect:/home#nav-credential";

        }else{
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", "Error updating credentials!");
            System.out.println("Error updating credentials!");
        }
    }

        return "home";
//        return "redirect:/result?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredentials(@PathVariable("id") int credentialId, Model model) {
        System.out.println("credentialId PARAM===" + credentialId);
        //find credentialId first then will pass it to deleteCredentials()
        Credential credential = credentialsService.getCredential(credentialId);
        if(credentialsService.getCredentials(credentialId) != null){
            credentialsService.deleteCredentials(credentialId);
            model.addAttribute("success", true);
            model.addAttribute("successMessage", "Credentials deleted successfully!");
            //display the success message by hitting the /result endpoint
//            return "redirect:/result?success";

        } else {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", "Error deleting credentials!");
            System.out.println("Error deleting credentials!");
            return "redirect:/result?error";
        }
        return "redirect:/home#nav-credential";
    }

}
