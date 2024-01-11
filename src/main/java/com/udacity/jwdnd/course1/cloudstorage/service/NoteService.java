package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating NoteService bean");
    }

    public List<Note> getNotes(Integer userId) {
        System.out.println("Getting notes");
        return noteMapper.getNotes(userId);
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public void addNote(Note note) {
        System.out.println("Adding note");
        noteMapper.insertNote(note);
//        Note newNote = new Note();
//        newNote.setUserId(userId);
//        newNote.setNoteTitle(note.getNoteTitle());
//
//        newNote.setNoteDescription(note.getNoteDescription());
//        System.out.println("---new note--->" + newNote.getNoteTitle());
//        noteMapper.insertNote(newNote);

    }

    public void deleteNote(Integer noteId) {
        System.out.println("Deleting note");
        noteMapper.delete(noteId);
    }

    public void updateNote(Note note) {
        System.out.println("Updating note");
        noteMapper.update(note);
    }

}
