package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoteUpdateService {
    private final NoteService noteService;

    @Autowired
    public NoteUpdateService(NoteService noteService) {
        this.noteService = noteService;
    }

    public void updateNote(Note note) {
        System.out.println("----NoteUpdateService.updateNote called...");
        noteService.updateNote(note);
    }
}
