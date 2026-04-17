package com.example.istream;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertItem(PlaylistItem item);

    @Query("SELECT * FROM playlist_items WHERE username = :username")
    List<PlaylistItem> getPlaylistForUser(String username);

    @Query("DELETE FROM playlist_items WHERE id = :id")
    void deleteItem(int id);
}
