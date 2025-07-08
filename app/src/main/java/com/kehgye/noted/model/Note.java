package com.kehgye.noted.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes", indices = {
        @Index(value = {"timestamp"}),
        @Index(value = {"isPinned"}),
        @Index(value = {"isTrashed"})
})
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "lastEdited")
    private long lastEdited;

    @ColumnInfo(name = "isPinned")
    private boolean isPinned;

    @ColumnInfo(name = "isTrashed")
    private boolean isTrashed;

    // ✅ Constructor used by Room (do NOT annotate this one)
    public Note(String title, String content, boolean isPinned, boolean isTrashed, long timestamp) {
        this.title = title;
        this.content = content;
        this.isPinned = isPinned;
        this.isTrashed = isTrashed;
        this.timestamp = timestamp;
    }

    // ❌ Constructor ignored by Room (used internally only)
    @Ignore
    public Note(String title, String content, long timestamp, long lastEdited,
                boolean isPinned, boolean isTrashed) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.lastEdited = lastEdited;
        this.isPinned = isPinned;
        this.isTrashed = isTrashed;
    }

    // ----- GETTERS & SETTERS -----

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public long getLastEdited() { return lastEdited; }
    public void setLastEdited(long lastEdited) { this.lastEdited = lastEdited; }

    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }

    public boolean isTrashed() { return isTrashed; }
    public void setTrashed(boolean trashed) { isTrashed = trashed; }
}