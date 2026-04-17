package com.example.istream;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist_items")
public class PlaylistItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public String videoUrl;

    public PlaylistItem(String username, String videoUrl) {
        this.username = username;
        this.videoUrl = videoUrl;
    }
}
