# iStream 📺

A Personal Video Playlist Android application built as part of SIT708 Task 5.1 at Deakin University.

## Overview

iStream is an Android app that allows users to create accounts, log in, and build a personal YouTube video playlist. Each user's playlist is stored separately in a local Room database. Videos are played directly in-app using a WebView.

## Features

- **Authentication**
  - Login screen with username and password
  - Sign up screen collecting full name, username, password, and confirm password
  - Duplicate username detection
  - Password match and length validation
  - Session persistence using SharedPreferences

- **Home Screen**
  - Enter any YouTube URL and play it in-app via WebView
  - Progress bar shown while video loads
  - Add current URL to personal playlist
  - Navigate to playlist screen
  - Logout button

- **Playlist Screen**
  - Displays all saved URLs for the logged-in user
  - Tap any URL to load it in the player on the home screen
  - Delete individual items from the playlist
  - Empty state message when playlist is empty
  - Logout button

- **Security**
  - Each user's playlist is stored separately — users cannot see each other's playlists
  - Invalid or non-YouTube URLs are rejected with an error message
  - Session is cleared on logout

## Tech Stack

| Component | Technology |
|---|---|
| Language | Java |
| UI | XML Layouts, Material Components |
| Navigation | Activity-based navigation |
| Database | Room (SQLite) |
| Session | SharedPreferences |
| Video Playback | WebView (YouTube mobile) |
| Min SDK | API 24 |
| Target SDK | API 36 |

## Project Structure

LoginActivity.java — Login screen with credential validation

SignUpActivity.java — Sign up screen with form validation and duplicate check

HomeActivity.java — Home screen with WebView player and playlist controls

PlaylistActivity.java — Playlist screen showing saved URLs for logged-in user

PlaylistAdapter.java — RecyclerView adapter for playlist items with delete support

User.java — Room entity for user accounts

PlaylistItem.java — Room entity for playlist URLs

UserDao.java — DAO for user insert and login queries

PlaylistDao.java — DAO for playlist insert, fetch, and delete queries

AppDatabase.java — Room database singleton instance

SessionManager.java — SharedPreferences helper for login session

## How to Run

1. Clone the repository:
```bash
   git clone https://github.com/YOUR_USERNAME/iStream.git
```
2. Open the project in **Android Studio**
3. Let Gradle sync complete
4. Run on an emulator or physical device with **API 24 or higher**

## Screenshots

| Login Screen | Home Screen | Playlist Screen |
|---|---|---|
| <img width="271" height="601" alt="image" src="https://github.com/user-attachments/assets/43298be1-25e7-468a-873b-7dac30c2878c" /> | <img width="276" height="609" alt="image" src="https://github.com/user-attachments/assets/1189fd11-4eda-4ba3-95d0-8b5bded6b870" />| <img width="274" height="603" alt="image" src="https://github.com/user-attachments/assets/fba102d7-89b2-4710-b494-738f19496664" />|

## AI Assistance Declaration

This project was completed with limited supplementary assistance from Claude (Anthropic). See the full LLM Declaration Statement in the submission document for details.

## Author

**Biku** — SIT708, Deakin University
