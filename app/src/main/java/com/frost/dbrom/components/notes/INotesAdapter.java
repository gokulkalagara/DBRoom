package com.frost.dbrom.components.notes;

import com.frost.dbrom.database.Note;

/**
 * Created by Gokul Kalagara (Mr. Psycho) on 09-08-2019.
 * <p>
 * Frost
 */
public interface INotesAdapter
{
    public void deleteNote(Note note,int position);
    public void updateNote(Note note,int position);
}
