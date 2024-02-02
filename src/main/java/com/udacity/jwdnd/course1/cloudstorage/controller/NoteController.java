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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/home/notes")
public class NoteController {

    private UserService userService;
    private NoteService noteService;
    private final NoteUpdateService noteUpdateService;
    private UserMapper userMapper;
    public NoteMapper noteMapper;
    private String errorMessage = null;
    private String successMessage = null;

    @Autowired
    public NoteController(NoteService noteService, UserService userService, NoteMapper noteMapper, NoteUpdateService noteUpdateService){
        this.noteService = noteService;
        this.noteUpdateService = noteUpdateService;
        this.userService = userService;
        this.noteMapper = noteMapper;
        System.out.println("NoteController initialized with NoteService: " + noteService);
//        this.userMapper = userMapper;
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
        return "redirect:/result?success";
    }

    @PostMapping("/notes")
    public String addNote(Authentication authentication, @ModelAttribute Note note){
        User user = userService.getUser(authentication.getName());
        System.out.printf("user: %s%n", user);
        Integer userId = user.getUserId();
        System.out.println("LOG +++ addNote +++ - noteId value: " + note.getNoteId());

        if(note.getNoteId() == null){
            System.out.println("1.noteId is null, creating new note...");
            note.setUserId(userId);
            noteService.addNote(note);
        }
//        else{
//            System.out.println("2.noteId is NOT null, updating existing note...");
//            noteService.updateNote(note);
//        }
        System.out.println("3. redirecting to success message...");

        return "redirect:/result?success";//if noteId is null, it will redirect to /result?success
    }

    @PutMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateNoteEndpoint(@PathVariable int id, @Valid @RequestBody Note note) throws Exception {
        Note existingNote = noteService.getNote(id);
        System.out.println("****existingNote: " + existingNote);
        if (existingNote != null && !existingNote.equals(note)) {
            System.out.println("^^^^^^^^existingNote is not null and not equal to note");
            throw new Exception("The requested resource does not exist...");
        }
        noteService.updateNote(note);
//        noteUpdateService.updateNote(note);
        return ResponseEntity.ok().build();
    }
//    @PutMapping("/notes")
//    public String updateNote(@Validated @RequestBody Note note, Authentication authentication){
//        System.out.println("*** updateNote *** - note object: " + note);
//        User user = userService.getUser(authentication.getName());
//        Integer userId = user.getUserId();
//        System.out.println("userId...editing: " + userId);
//        note.setUserId(userId);
//        note.setNoteId(note.getNoteId());
//        System.out.println("noteId...editing: " + note.getNoteId());
////        return String.valueOf(noteMapper.update(note));
//        noteService.updateNote(note);//note title or description is not being updated in note service
//        return "redirect:/result?success";
//    }

}
