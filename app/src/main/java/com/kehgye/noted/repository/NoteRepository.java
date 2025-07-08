package com.kehgye.noted.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.kehgye.noted.data.NoteDao;
import com.kehgye.noted.data.NoteDatabase;
import com.kehgye.noted.model.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private final NoteDao noteDao;
    private final LiveData<List<Note>> allNotes;
    private final ExecutorService executorService;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
        executorService = Executors.newFixedThreadPool(2); // For background DB work
    }

    public void insert(Note note) {
        executorService.execute(() -> noteDao.insert(note));
    }

    public void update(Note note) {
        executorService.execute(() -> noteDao.update(note));
    }

    public void delete(Note note) {
        executorService.execute(() -> noteDao.delete(note));
    }

    public void deleteTrashedNotes() {
        executorService.execute(noteDao::deleteAllTrashedNotes);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<Note> getNoteById(int id) {
        return noteDao.getNoteById(id);
    }
}