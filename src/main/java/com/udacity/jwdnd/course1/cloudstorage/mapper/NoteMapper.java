package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES")
    List<Note> getAllNotes();

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getNoteById(Integer noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userId) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer insertNote(Note note);

    //get note byt title
    @Select("SELECT * FROM NOTES WHERE notetitle = #{noteTitle}")
    Note getNoteByTitle(String noteTitle);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
//    @Delete("DELETE FROM NOTES WHERE noteTitle = #{noteTitle}")
    int delete(Integer noteId);
//    int delete(String noteTitle);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    int update(Note note);

    @Select("SELECT * FROM NOTES WHERE notetitle = #{noteTitle}")
    boolean isNote(String noteTitle);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getUserNotes(Integer userId);
}
