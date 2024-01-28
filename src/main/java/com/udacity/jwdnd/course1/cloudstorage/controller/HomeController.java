package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private UserService userService;
    private FileService fileService;
    private CredentialsService credentialsService;
    private NoteService noteService;
//    private UserMapper userMapper;
    private EncryptionService encryptionService;

    //fix redirect to home page after successful--
    public HomeController(UserService userService, FileService fileService, CredentialsService credentialsService, NoteService noteService, EncryptionService encryptionService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialsService = credentialsService;
//        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }
    @GetMapping("/home")
    public String homePage(Authentication authentication, Model model, File file, Note note, Credentials credentials) {
        User user = userService.getUser(authentication.getName());
        Integer userId = user.getUserId();

        List<Note> noteList = noteService.getNotes(userId);
        model.addAttribute("notes", noteList);
        List<File> fileList = fileService.getFiles(userId);
        model.addAttribute("files", fileList);
        List<Credentials> credentialsList = credentialsService.getCredentials(userId);
        model.addAttribute("credentials", credentialsList);
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
