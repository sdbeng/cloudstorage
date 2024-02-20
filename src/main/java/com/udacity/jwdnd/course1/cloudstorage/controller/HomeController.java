package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private FileService fileService;
    private CredentialsService credentialsService;
    private NoteService noteService;
//    private UserMapper userMapper;
    private EncryptionService encryptionService;

    public HomeController(UserService userService, FileService fileService, CredentialsService credentialsService, NoteService noteService, EncryptionService encryptionService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialsService = credentialsService;
//        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }
    @GetMapping
    public String homePage(Authentication authentication, Model model, File file, Note note, CredentialForm credentialForm){
        User user = userService.getUser(authentication.getName());
//        Integer userId = user.getUserId();

        List<Note> noteList = noteService.getAllNotes();
        model.addAttribute("notes", noteList);
        List<File> fileList = fileService.getFiles(user.getUserId());
        model.addAttribute("files", fileList);
        model.addAttribute("credentials", credentialsService.getCredentials(user.getUserId()));
//        List<Credential> credentialsList = credentialsService.getCredentials();
//        model.addAttribute("credentials", credentialsList);
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

    @GetMapping("/result")
    public String result() {
        System.out.println("result mapped path...");
        return "redirect:/home#nav-credential";
//        return "result";
    }
}
