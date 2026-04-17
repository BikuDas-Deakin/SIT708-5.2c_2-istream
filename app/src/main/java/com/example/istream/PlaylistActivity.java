package com.example.istream;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private PlaylistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        String username = SessionManager.getLoggedInUsername(this);
        TextView tvTitle = findViewById(R.id.tv_playlist_title);
        tvTitle.setText(username + "'s Playlist");

        RecyclerView recyclerView = findViewById(R.id.rv_playlist);
        TextView tvEmpty = findViewById(R.id.tv_empty_playlist);
        Button btnLogout = findViewById(R.id.btn_logout_playlist);
        Button btnBack = findViewById(R.id.btn_back_playlist);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        btnLogout.setOnClickListener(v -> {
            SessionManager.clearSession(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        loadPlaylist(username, recyclerView, tvEmpty);
    }

    private void loadPlaylist(String username, RecyclerView recyclerView, TextView tvEmpty) {
        executor.execute(() -> {
            List<PlaylistItem> items = AppDatabase.getInstance(this).playlistDao().getPlaylistForUser(username);
            runOnUiThread(() -> {
                if (items.isEmpty()) {
                    tvEmpty.setVisibility(android.view.View.VISIBLE);
                    recyclerView.setVisibility(android.view.View.GONE);
                } else {
                    tvEmpty.setVisibility(android.view.View.GONE);
                    recyclerView.setVisibility(android.view.View.VISIBLE);
                    adapter = new PlaylistAdapter(items,
                            item -> {
                                // Open in HomeActivity
                                Intent intent = new Intent(this, HomeActivity.class);
                                intent.putExtra("video_url", item.videoUrl);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                            },
                            item -> {
                                // Delete item
                                executor.execute(() -> {
                                    AppDatabase.getInstance(this).playlistDao().deleteItem(item.id);
                                    runOnUiThread(() -> {
                                        Toast.makeText(this, "Removed from playlist", Toast.LENGTH_SHORT).show();
                                        loadPlaylist(username, recyclerView, tvEmpty);
                                    });
                                });
                            });
                    recyclerView.setAdapter(adapter);
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
