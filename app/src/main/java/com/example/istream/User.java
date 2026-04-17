package com.example.istream;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    public String username;

    public String fullName;
    public String password;

    public User(@NonNull String username, String fullName, String password) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
    }
}
