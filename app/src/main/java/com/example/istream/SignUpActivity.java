package com.example.istream;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFullName, etUsername, etPassword, etConfirmPassword;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etFullName = findViewById(R.id.et_full_name);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        Button btnCreate = findViewById(R.id.btn_create_account);

        btnCreate.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 4) {
                Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                // Check if username already exists
                User existing = AppDatabase.getInstance(this).userDao().getUserByUsername(username);
                runOnUiThread(() -> {
                    if (existing != null) {
                        Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
                    } else {
                        executor.execute(() -> {
                            User newUser = new User(username, fullName, password);
                            long result = AppDatabase.getInstance(this).userDao().insertUser(newUser);
                            runOnUiThread(() -> {
                                if (result != -1) {
                                    Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, LoginActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(this, "Sign up failed. Try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    }
                });
            });
        });

        findViewById(R.id.tv_back_to_login).setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
