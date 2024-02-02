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

//    @PostConstruct
//    public void postConstruct() {
//        System.out.println("Creating NoteService bean");
//    }

    public List<Note> getNotes(Integer userId) {
        String checkUserId = userId == null ? "null" : userId.toString();
        System.out.println("Getting notes, checkUserId: " + checkUserId);
        return noteMapper.getNotes(userId);
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public Integer addNote(Note note) {
        //check if note is null
        if (note == null) {
            System.out.println("note is null");
        } else {
            System.out.println("NOteService:note is NOT null- note title:"+note.getNoteTitle());
        }
        System.out.println("Adding note...NoteService");
        return noteMapper.insertNote(note);//this was returning null
//        Note newNote = new Note();
//        newNote.setUserId(userId);
//        newNote.setNoteTitle(note.getNoteTitle());
//
//        newNote.setNoteDescription(note.getNoteDescription());
//        System.out.println("---new note--->" + newNote.getNoteTitle());
//        noteMapper.insertNote(newNote);

    }

    public void deleteNote(Integer noteId) {
        System.out.println("Deleting note...");
        noteMapper.delete(noteId);
    }

    public Integer updateNote(Note note) {
        System.out.println("^^^note object passed in updateNote ^^^"+ note);
        System.out.printf("Updating note...%s%n", note.getNoteTitle());//even tho note seems to be updated through a success msg, it's not reaching here, title or desc won't change
        return noteMapper.update(note);
    }

}
