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
    private int noteId = -1; // default = new note

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        editTextTitle = findViewById(R.id.editText_title);
        editTextContent = findViewById(R.id.editText_content);
        Button buttonSave = findViewById(R.id.button_save);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // Check if we received existing note data
        if (getIntent().hasExtra("note_id")) {
            noteId = getIntent().getIntExtra("note_id", -1);
            String title = getIntent().getStringExtra("note_title");
            String content = getIntent().getStringExtra("note_content");

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

        Note note = new Note(title, content, false, false, System.currentTimeMillis());

        if (noteId != -1) {
            note.setId(noteId); // very important to set ID
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            noteViewModel.insert(note);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }

        finish(); // go back to MainActivity
    }
}