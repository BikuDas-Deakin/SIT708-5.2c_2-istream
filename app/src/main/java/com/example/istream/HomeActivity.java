package com.example.istream;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private WebView webView;
    private EditText etVideoUrl;
    private ProgressBar progressBar;
    private String currentUrl = "";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SessionManager.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_home);

        etVideoUrl = findViewById(R.id.et_video_url);
        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progress_bar);
        Button btnPlay = findViewById(R.id.btn_play);
        Button btnAddToPlaylist = findViewById(R.id.btn_add_to_playlist);
        Button btnMyPlaylist = findViewById(R.id.btn_my_playlist);
        Button btnLogout = findViewById(R.id.btn_logout);

        setupWebView();

        // Pre-fill URL if passed from playlist screen
        String passedUrl = getIntent().getStringExtra("video_url");
        if (passedUrl != null && !passedUrl.isEmpty()) {
            etVideoUrl.setText(passedUrl);
            loadVideo(passedUrl);
        }

        btnPlay.setOnClickListener(v -> {
            String url = etVideoUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter a YouTube URL", Toast.LENGTH_SHORT).show();
                return;
            }
            loadVideo(url);
        });

        btnAddToPlaylist.setOnClickListener(v -> {
            String url = etVideoUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(this, "Enter a URL first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidYouTubeUrl(url)) {
                Toast.makeText(this, "Please enter a valid YouTube URL", Toast.LENGTH_SHORT).show();
                return;
            }
            String username = SessionManager.getLoggedInUsername(this);
            executor.execute(() -> {
                PlaylistItem item = new PlaylistItem(username, url);
                AppDatabase.getInstance(this).playlistDao().insertItem(item);
                runOnUiThread(() -> Toast.makeText(this, "Added to playlist!", Toast.LENGTH_SHORT).show());
            });
        });

        btnMyPlaylist.setOnClickListener(v -> {
            startActivity(new Intent(this, PlaylistActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            SessionManager.clearSession(this);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        settings.setUserAgentString(
                "Mozilla/5.0 (Linux; Android 10; Pixel 4) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/120.0.0.0 Mobile Safari/537.36"
        );

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(newProgress < 100 ? View.VISIBLE : View.GONE);
            }
        });
        webView.setWebViewClient(new WebViewClient());
    }

    private void loadVideo(String url) {
        String videoId = extractVideoId(url);
        if (videoId == null) {
            Toast.makeText(this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        currentUrl = url;
        webView.loadUrl("https://m.youtube.com/watch?v=" + videoId);
    }

    private String extractVideoId(String url) {
        if (url == null || url.isEmpty()) return null;
        if (url.contains("youtu.be/")) {
            String[] parts = url.split("youtu.be/");
            if (parts.length > 1) {
                return parts[1].split("[?&]")[0];
            }
        }
        if (url.contains("v=")) {
            String[] parts = url.split("v=");
            if (parts.length > 1) {
                return parts[1].split("[?&]")[0];
            }
        }
        return null;
    }

    private boolean isValidYouTubeUrl(String url) {
        return url.contains("youtube.com") || url.contains("youtu.be");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = intent.getStringExtra("video_url");
        if (url != null && !url.isEmpty()) {
            etVideoUrl.setText(url);
            loadVideo(url);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}