package com.udacity.jwdnd.course1.cloudstorage.service;

import org.slf4j.Logger;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;
    private Logger logger = org.slf4j.LoggerFactory.getLogger(NoteService.class);

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNotes() {
        System.out.println("Getting all notes...");
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

    public Integer addNoteService(Note note) {
        logger.info("addNoteService..."+ note);
        //check if note is null
//        if (note == null) {
//            logger.info("xxxxxx note is null xxxxxx");
//
//        } else {
//            logger.info("NOteService:note is NOT null- note title:"+note.getNoteTitle());
//        }
//        logger.info("Creating new note...");

        Note newNote = new Note();
//        newNote.setUserId(userId);
        newNote.setNoteTitle(note.getNoteTitle());
        newNote.setNoteDescription(note.getNoteDescription());
        logger.info("---new note--->" + newNote.getNoteTitle());
//        int result = noteMapper.insertNote(newNote);
//        logger.info("NoteService: new note INSERted, result: "+ result);
        return noteMapper.insertNote(newNote);
    }

    public int deleteNote(Integer noteId) {
        System.out.println("Deleting note..." + noteId);
        return noteMapper.delete(noteId);
    }
//    public int deleteNote(String noteTitle) {
//        System.out.println("Deleting note..." + noteTitle);
//        return noteMapper.delete(noteTitle);
//    }

    public int updateNote(Note note) {
        System.out.println("^^^note object passed in updateNote ^^^"+ note);
        Note newNote = new Note();
        newNote.setUserId(note.getUserId());
        newNote.setNoteId(note.getNoteId());
        newNote.setNoteTitle(note.getNoteTitle());
        System.out.printf("Updating note...%s%n", note.getNoteTitle());
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
