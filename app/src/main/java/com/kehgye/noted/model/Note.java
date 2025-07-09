package com.kehgye.noted.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "notes",
        indices = {
                @Index(value = {"createdAt"}),
                @Index(value = {"lastEdited"}),
                @Index(value = {"isPinned"}),
                @Index(value = {"isTrashed"})
        }
)
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "createdAt")
    private long createdAt;

    @ColumnInfo(name = "lastEdited")
    private long lastEdited;

    @ColumnInfo(name = "isPinned")
    private boolean isPinned;

    @ColumnInfo(name = "isTrashed")
    private boolean isTrashed;

    // ✅ Constructor used by Room
    public Note(String title, String content, boolean isPinned, boolean isTrashed, long createdAt, long lastEdited) {
        this.title = title;
        this.content = content;
        this.isPinned = isPinned;
        this.isTrashed = isTrashed;
        this.createdAt = createdAt;
        this.lastEdited = lastEdited;
    }

    // ✅ Convenience constructor (ignored by Room)
    @Ignore
    public Note(String title, String content) {
        long now = System.currentTimeMillis();
        this.title = title;
        this.content = content;
        this.createdAt = now;
        this.lastEdited = now;
        this.isPinned = false;
        this.isTrashed = false;
    }

    // ----- GETTERS & SETTERS -----

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getLastEdited() { return lastEdited; }
    public void setLastEdited(long lastEdited) { this.lastEdited = lastEdited; }

    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }

    public boolean isTrashed() { return isTrashed; }
    public void setTrashed(boolean trashed) { isTrashed = trashed; }
}