package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.slf4j.Logger;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;
    private UserMapper userMapper;
    private Logger logger = org.slf4j.LoggerFactory.getLogger(NoteService.class);

    public NoteService(NoteMapper noteMapper){
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNotes() {
        return noteMapper.getAllNotes();
    }

    public List<Note> getUserNotes(Integer userId) {
        return noteMapper.getUserNotes(userId);
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public boolean isNoteAvailable(String noteTitle) {
        return noteMapper.getNoteByTitle(noteTitle) == null;
    }

    public Integer addNoteService(Note note, Integer userId) {
        System.out.println("Add note service, userId passed: " + userId);
        Note newNote = new Note();
        newNote.setUserId(userId);
        newNote.setNoteTitle(note.getNoteTitle());
        newNote.setNoteDescription(note.getNoteDescription());
        return noteMapper.insertNote(newNote);
    }

    public int deleteNote(Integer noteId) {
        System.out.println("Deleting note..." + noteId);
        return noteMapper.delete(noteId);
    }

    public int updateNote(Note note) {
        Note newNote = new Note();
        newNote.setUserId(note.getUserId());
        newNote.setNoteId(note.getNoteId());
        newNote.setNoteTitle(note.getNoteTitle());
        newNote.setNoteDescription(note.getNoteDescription());
        return noteMapper.update(note);
    }

    public int editNoteById(Note note) {
        return noteMapper.update(note);
    }

    public Note getNoteById(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public void deleteNoteById(Integer noteId) {
        noteMapper.delete(noteId);
    }
}
