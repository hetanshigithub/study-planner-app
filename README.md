# Study Planner App 📚

An Android productivity app that helps students manage tasks, subjects, and due dates with a clean, modern interface.

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM (ViewModel + StateFlow)
- **Local Storage:** Room Database + DataStore
- **Navigation:** Jetpack Navigation Component

## Features
- Add, edit, and delete study tasks
- Set due dates and priority levels
- Filter and sort tasks by due date
- Persistent storage — data survives app restarts
- Clean Material 3 design with accessibility support

## Architecture
The app follows MVVM architecture:
- **UI Layer** — Composable screens observe StateFlow from ViewModel
- **ViewModel** — Holds UI state and handles user actions
- **Repository** — Single source of truth, abstracts Room DAO
- **Room Database** — Local SQLite persistence via Entity/DAO pattern

## Screenshots
<img width="1080" height="2400" alt="Screenshot_20260618_175730" src="https://github.com/user-attachments/assets/39c724a8-5c2a-478c-b767-fca0b09ad949" />
<img width="1470" height="956" alt="Screenshot 2026-06-18 at 6 07 42 PM" src="https://github.com/user-attachments/assets/c1f78879-eb40-4af3-a426-e88d27d1518c" />
<img width="1470" height="956" alt="Screenshot 2026-06-18 at 6 08 27 PM" src="https://github.com/user-attachments/assets/a0f666ce-2b45-4d8b-b75c-89a4dbb4a33d" />
<img width="1470" height="956" alt="Screenshot 2026-06-18 at 6 08 15 PM" src="https://github.com/user-attachments/assets/1f185610-53fd-418a-b1d4-8d885f55819c" />





## How to Run
1. Clone the repo
2. Open in Android Studio
3. Run on emulator or physical device (API 26+)
