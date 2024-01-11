package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home/notes")
public class NoteController {

    private UserService userService;
    private NoteService noteService;
    private UserMapper userMapper;
    private String errorMessage = null;
    private String successMessage = null;


    public NoteController(NoteService noteService, UserService userService, UserMapper userMapper) {
        this.noteService = noteService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") Integer noteId) {
//        if(noteId == null){
//            errorMessage = "Note not found";
//
//            return "redirect:/home";
//        }
        noteService.deleteNote(noteId);
//        successMessage = "Note deleted successfully";

//        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/result";
    }

    @PostMapping("/notes")
    public String addOrUpdateNote(Authentication authentication, @ModelAttribute Note note){
        User user = userService.getUser(authentication.getName());
        Integer userId = user.getUserId();

        if(note.getNoteId() == null){
            note.setUserId(userId);
            noteService.addNote(note);
        }else{
            noteService.updateNote(note);
        }
        return "redirect:/home?success";
    }

}
