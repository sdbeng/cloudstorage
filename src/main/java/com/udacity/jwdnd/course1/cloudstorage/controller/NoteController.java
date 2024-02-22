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
        System.out.println("NoteController initialized with NoteService: " + noteService);
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
        note.setUserId(userService.getUser(authentication.getName()).getUserId());
        String errorMessage = null;
        System.out.printf("###### user: %s%n", user);
        System.out.printf("userId: %s%n", user.getUserId());
//        System.out.println("LOG +++ addNote +++ - noteId value: " + note.getNoteId());

//        if(note.getNoteTitle().isEmpty() || note.getNoteDescription().isEmpty()){
//            errorMessage = "Note title and description cannot be empty.";
//        }
//        if(!noteService.isNoteAvailable(note.getNoteTitle())) {
//            errorMessage = "Oops, the note already exists.";
//        }

//        if (errorMessage == null) {
//            if (noteService.addNoteService(note) != 1) {
//                errorMessage = "There was an error creating the node. Please try again.";
//            }
//        }

        System.out.println("NoteController: noteId: " + note.getNoteId());
        if(note.getNoteId() == null){
            try {
                noteService.addNoteService(note);
                System.out.println("NoteController: noteId null, note added...");
//                model.addAttribute("isSuccess", true);
//                model.addAttribute("successMsg", "Note has been created!");
            }catch (Exception e){
                System.out.println("There is an error adding a note: " + e);
                model.addAttribute("isError", true);
//                model.addAttribute("errorMsg", "Something wrong when creating a Note.");
//                errorMessage = "An error occurred during creation of a Note.";
            }
        }else{
            System.out.println("----Note does exist, editing note..."+ note.getNoteTitle());
            noteService.updateNote(note);
//            model.addAttribute("isSuccess", true);
//            model.addAttribute("successMsg", "Note has been updated!");
        }

        model.addAttribute("notes", noteService.getAllNotes());//returning all notes
//        return "redirect:/result?success";
        //display success message
        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String handleDeleteNote(@PathVariable(value="id") Integer noteId,  Model model) {
        Note note = noteService.getNoteById(noteId);
        System.out.println("get note by id: " + note.getNoteId());
        System.out.println("*********NoteController: deleting noteId: ===" + noteId);
        noteService.deleteNoteById(noteId);
//    public String deleteFile(@RequestParam(value="id", required = false) String noteTitle,  Model model) {
//        System.out.println("*********NoteController: deleting noteId: ===" + noteId);
//
//        noteService.deleteNote(noteId);
//        noteService.deleteNote(noteTitle);
        model.addAttribute("notes", this.noteService.getAllNotes());
        //return to notes tab route
        return "redirect:/home";
    }

}
