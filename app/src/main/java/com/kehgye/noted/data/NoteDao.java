package com.kehgye.noted.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kehgye.noted.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    // ✅ Show only non-trashed notes, pinned on top, sorted by last edited time
    @Query("SELECT * FROM notes WHERE isTrashed = 0 ORDER BY isPinned DESC, lastEdited DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    LiveData<Note> getNoteById(int id);

    // ✅ Permanently delete all trashed notes
    @Query("DELETE FROM notes WHERE isTrashed = 1")
    void deleteAllTrashedNotes();

    // ✅ Move a note to trash using ID
    @Query("UPDATE notes SET isTrashed = 1 WHERE id = :id")
    void trashNoteById(int id);

    // ✅ Fetch trashed notes sorted by last edited time
    @Query("SELECT * FROM notes WHERE isTrashed = 1 ORDER BY lastEdited DESC")
    LiveData<List<Note>> getTrashedNotes();

    @Query("SELECT * FROM notes WHERE isPinned = 1 AND isTrashed = 0 ORDER BY lastEdited DESC")
    LiveData<List<Note>> getPinnedNotes();
}