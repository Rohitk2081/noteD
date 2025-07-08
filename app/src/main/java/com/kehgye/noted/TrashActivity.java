package com.kehgye.noted;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kehgye.noted.adapter.NoteAdapter;
import com.kehgye.noted.model.Note;
import com.kehgye.noted.viewmodel.NoteViewModel;

public class TrashActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView_trash);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        noteViewModel.getTrashedNotes().observe(this, notes -> {
            if (notes != null) {
                adapter.submitList(notes);
            }
        });

        // Short click → info
        adapter.setOnItemClickListener(note -> {
            Toast.makeText(this, "Long press to Restore or Delete", Toast.LENGTH_SHORT).show();
        });

        // Long press → Restore/Delete
        adapter.setOnItemLongClickListener(note -> {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Manage Note")
                    .setMessage("What do you want to do with this note?")
                    .setPositiveButton("Restore", (dialog, which) -> {
                        note.setTrashed(false);
                        noteViewModel.update(note);
                        Toast.makeText(this, "Note restored", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Delete Forever", (dialog, which) -> {
                        noteViewModel.delete(note);
                        Toast.makeText(this, "Note permanently deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNeutralButton("Cancel", null)
                    .show();
        });
    }
}