package com.kehgye.noted.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kehgye.noted.model.Note;
import com.kehgye.noted.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository repository;
    private final LiveData<List<Note>> allNotes;
    private final LiveData<List<Note>> trashedNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
        trashedNotes = repository.getTrashedNotes(); // ✅ Fixed here
    }

    // Expose LiveData to UI
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getTrashedNotes() {
        return trashedNotes;
    }

    public LiveData<Note> getNoteById(int id) {
        return repository.getNoteById(id);
    }

    // UI calls these methods
    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteTrashedNotes() {
        repository.deleteTrashedNotes();
    }

    public void trashNote(Note note) {
        repository.trashNote(note);
    }
    public LiveData<List<Note>> getPinnedNotes() {
        return repository.getPinnedNotes();
    }
}