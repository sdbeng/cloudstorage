package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteUpdateService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private UserService userService;
    private NoteService noteService;
    private final NoteUpdateService noteUpdateService;
    private UserMapper userMapper;
    public NoteMapper noteMapper;
    private String errorMessage = null;
    private String successMessage = null;

    @Autowired
    public NoteController(NoteService noteService, UserService userService, NoteMapper noteMapper, NoteUpdateService noteUpdateService, UserMapper userMapper){
        this.noteService = noteService;
        this.noteUpdateService = noteUpdateService;
        this.userService = userService;
        this.noteMapper = noteMapper;
//        System.out.println("NoteController initialized with NoteService: " + noteService);
        this.userMapper = userMapper;
    }

    @GetMapping()
    public String getNotesList(Authentication authentication, Model model){
        User user = userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        System.out.println("userId: " + userId);
        model.addAttribute("notes", noteService.getAllNotes());
        return "home";
    }

    @PostMapping("/create")
    public String addNote(Authentication authentication, @ModelAttribute Note note, Model model){
        User user = userService.getUser(authentication.getName());
        System.out.printf("userId: %s%n", user.getUserId());

        if(note.getNoteId() == null){
                noteService.addNoteService(note, user.getUserId());
                model.addAttribute("success", true);
                model.addAttribute("successMsg", "Note has been created!");
        }else{
            noteService.updateNote(note);
            model.addAttribute("update", true);
            model.addAttribute("updateMsg", "Note has been updated!");
        }
        model.addAttribute("notes", noteService.getAllNotes());
        return "redirect:/home/note-success";
    }

    @GetMapping("/delete/{id}")
    public String handleDeleteNote(@PathVariable(value="id") Integer noteId,  Model model) {
        Note note = noteService.getNoteById(noteId);
        noteService.deleteNoteById(noteId);
        model.addAttribute("notes", this.noteService.getAllNotes());
        model.addAttribute("success", true);
        return "redirect:/home/#nav-notes";
    }
}
