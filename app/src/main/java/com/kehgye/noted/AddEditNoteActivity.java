package com.kehgye.noted;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.kehgye.noted.model.Note;
import com.kehgye.noted.viewmodel.NoteViewModel;

public class AddEditNoteActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextContent;
    private NoteViewModel noteViewModel;
    private int noteId = -1;
    private long originalTimestamp = 0L; // For editing existing note
    private long originalCreatedAt = -1; // default value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        editTextTitle = findViewById(R.id.editText_title);
        editTextContent = findViewById(R.id.editText_content);
        Button buttonSave = findViewById(R.id.button_save);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // Check if editing an existing note
        if (getIntent().hasExtra("note_id")) {
            noteId = getIntent().getIntExtra("note_id", -1);
            String title = getIntent().getStringExtra("note_title");
            String content = getIntent().getStringExtra("note_content");
            originalTimestamp = getIntent().getLongExtra("note_created_at", System.currentTimeMillis());

            editTextTitle.setText(title);
            editTextContent.setText(content);
        }

        buttonSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        long now = System.currentTimeMillis();

        if (noteId != -1) {
            // Existing note: keep createdAt, update lastEdited
            // Corrected constructor order
            Note updatedNote = new Note(title, content, false, false, originalTimestamp, now);
            updatedNote.setId(noteId);
            noteViewModel.update(updatedNote);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            // New note: use same timestamp for both fields
            Note newNote = new Note(title, content, false, false, now, now);
            noteViewModel.insert(newNote);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }

        finish(); // Close activity
    }
}