package com.frost.dbrom.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Created by Gokul Kalagara (Mr. Psycho) on 08-08-2019.
 * <p>
 * Frost
 */
@Dao
public interface NoteDao
{
    @Insert
    public void insert(Note note);

    @Query("Select * from note ORDER BY id DESC")
    public List<Note> getAllNotes();

    @Query("Delete from note")
    public void deleteAllNotes();

    @Query("Delete from note where id = :id")
    public void deleteNoteById(int id);

    @Query("Update note set title = :title, description = :description where id = :id")
    public void updateNote(String title, String description, int id);
}
