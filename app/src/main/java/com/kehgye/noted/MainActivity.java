package com.kehgye.noted;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.kehgye.noted.adapter.NoteAdapter;
import com.kehgye.noted.model.Note;
import com.kehgye.noted.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    // 🔁 Current LiveData being observed
    private LiveData<List<Note>> currentLiveData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer setup
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // ViewModel setup
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.recyclerView_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        // Default: Observe all notes
        observeNotes(noteViewModel.getAllNotes());

        // FAB
        FloatingActionButton fab = findViewById(R.id.fab_add_note);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivity(intent);
        });

        // Navigation menu item clicks
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_notes) {
                observeNotes(noteViewModel.getAllNotes());
                Toast.makeText(this, "All Notes", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_pinned) {
                observeNotes(noteViewModel.getPinnedNotes());
                Toast.makeText(this, "Showing pinned notes", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_trash) {
                startActivity(new Intent(MainActivity.this, TrashActivity.class));
            } else if (id == R.id.nav_archive) {
                Toast.makeText(this, "Archive coming soon!", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_help) {
                Toast.makeText(this, "Feedback coming soon!", Toast.LENGTH_SHORT).show();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // Single Tap → Edit Note
        adapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            intent.putExtra("note_id", note.getId());
            intent.putExtra("note_title", note.getTitle());
            intent.putExtra("note_content", note.getContent());
            intent.putExtra("note_created_at", note.getCreatedAt());
            startActivity(intent);
        });

        // Long Press → Pin / Unpin / Trash
        adapter.setOnItemLongClickListener(note -> {
            boolean isCurrentlyPinned = note.isPinned();

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Manage Note")
                    .setMessage("What would you like to do?")
                    .setPositiveButton(isCurrentlyPinned ? "Unpin" : "Pin", (dialog, which) -> {
                        note.setPinned(!isCurrentlyPinned);
                        note.setLastEdited(System.currentTimeMillis());
                        noteViewModel.update(note);
                        adapter.submitList(null); // 🔁 reset diffutil cache
                        observeNotes(noteViewModel.getAllNotes()); // 🧠 reobserve updated list
                        Toast.makeText(this, isCurrentlyPinned ? "Note unpinned" : "Note pinned", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Move to Trash", (dialog, which) -> {
                        note.setTrashed(true);
                        note.setLastEdited(System.currentTimeMillis());
                        noteViewModel.update(note);
                        adapter.submitList(null); // 🔁 reset diffutil cache
                        observeNotes(noteViewModel.getAllNotes()); // 🧠 reobserve updated list
                        Toast.makeText(this, "Note moved to Trash", Toast.LENGTH_SHORT).show();
                    })
                    .setNeutralButton("Cancel", null)
                    .show();
        });
    }

    // ✅ LiveData switch helper
    private void observeNotes(LiveData<List<Note>> newLiveData) {
        if (currentLiveData != null) {
            currentLiveData.removeObservers(this);
        }

        currentLiveData = newLiveData;
        currentLiveData.observe(this, notes -> {
            adapter.submitList(notes);; // 🔁 force refresh instead of submitList
        });
    }
}