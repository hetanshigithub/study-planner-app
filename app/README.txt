Module 6 – Study Planner App
Student: Hetanshi Shah

1)App Overview
For this assignment I built a Study Planner application that helps students keep track of their study-related tasks. The main idea was to create a simple tool where I can add subjects, due dates, priorities, and keep everything organized in one place.
The app is developed using Kotlin, Jetpack Compose, Room Database, and DataStore Preferences. It supports the basic CRUD operations, remembers the user’s name and theme settings, and also includes a search feature that filters tasks instantly.

2)Main Features

Task Management
Add new study tasks (title, subject, due date, priority, completion flag)
View tasks in a scrollable list using LazyColumn
Edit an existing task (opens in the same dialog)
Delete tasks
Room database stores everything locally so tasks remain after closing the app

User Settings
Saves the username
Saves theme preference (dark/light mode)
These values are restored every time the app is launched

Jetpack Compose UI
Top AppBar showing the app title
Floating Action Button (+) to add a task
Compose dialog for adding/editing tasks
Search bar at the top for live filtering
Checkbox to filter “only incomplete tasks”
Smooth material design + animation when the task list appears or disappears

Bonus Features
Real-time search (list updates as you type)
AnimatedVisibility for empty list / non-empty list transitions
Completed/incomplete task status + filter toggle

3)Tech Stack Used
Language: Kotlin
UI Framework: Jetpack Compose with Material 3
Database: Room (DAO, Entity, Database)
Preferences: DataStore (not SharedPreferences)
Architecture: MVVM (ViewModel + Repository pattern)
Other: Kotlin coroutines, Flow for reactive updates

4)How to Run the Project
-Open the project folder in Android Studio.
-Allow Gradle to sync completely.
-Create or select an emulator (API Level 31+).
-Press the Run button.
-The app should install and appear on the emulator.
-If for some reason the home screen doesn’t show the app icon, reopen the app drawer.

5)How to Use the App (User Guide)
- When the app first opens, you’ll see a welcome message and a text field to write your name.
- Whatever name you save will appear every time because it's stored using DataStore.
- You can switch between Light and Dark mode with the theme toggle.
- Tap the + button to add a task → a dialog will show up.
- Enter task details such as title, subject, due date in milliseconds, priority (Low/Medium/High), and whether it's completed.
- Saved tasks appear in a list below.
- Tap Edit to modify a task or Del to delete it.
- Use the checkbox inside a task to mark it completed.
- The search bar filters tasks instantly by matching title or subject.
- There is also a “Show only incomplete tasks” filter to hide all completed tasks.

6)SQL Insight & Room Query Explanation
This is the part of the rubric asking for SQL understanding. Below is how the queries used inside my DAO translate to real SQL.

6.1 Entity → SQLite Table

My entity:
@Entity(tableName = "study_tasks")

This produces the SQLite table:
CREATE TABLE study_tasks (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    title TEXT NOT NULL,
    subject TEXT NOT NULL,
    dueDateMillis INTEGER NOT NULL,
    priority INTEGER NOT NULL,
    completed INTEGER NOT NULL
);

Every field in the StudyTask Kotlin data class becomes a column in this table.

6.2 Get All Tasks
Kotlin
@Query("SELECT * FROM study_tasks ORDER BY dueDateMillis ASC")

SQL
SELECT * FROM study_tasks
ORDER BY dueDateMillis ASC;

This returns all rows sorted by their due date.

6.3 Search Tasks by Title or Subject
Kotlin
@Query("""
    SELECT * FROM study_tasks
    WHERE title LIKE '%' || :query || '%'
       OR subject LIKE '%' || :query || '%'
    ORDER BY dueDateMillis ASC
""")

SQL
SELECT * FROM study_tasks
WHERE title LIKE '%keyword%'
   OR subject LIKE '%keyword%'
ORDER BY dueDateMillis ASC;

The LIKE operator allows partial matching (substring search).
This is why typing any phrase in the search bar instantly filters the list.

6.4 Insert a Task
Kotlin
@Insert
suspend fun insertTask(task: StudyTask)

SQL
INSERT INTO study_tasks (title, subject, dueDateMillis, priority, completed)
VALUES (?, ?, ?, ?, ?);

Room automatically injects the parameter values.

6.5 Update an Existing Task
Kotlin
@Update
suspend fun updateTask(task: StudyTask)

SQL
UPDATE study_tasks
SET title = ?, subject = ?, dueDateMillis = ?, priority = ?, completed = ?
WHERE id = ?;
This updatesthe row.

6.6 Delete a Task
Kotlin
@Delete
suspend fun deleteTask(task: StudyTask)

SQL
DELETE FROM study_tasks
WHERE id = ?;

This removes exactly one row that matches the task ID.

7)Architecture Notes
StudyPlannerViewModel contains the app’s state and logic for CRUD operations.
TaskRepository separates database logic from the ViewModel.
UserPreferencesRepository manages DataStore operations (username + theme).
UI observes changes using State, Flow, and Compose states.
This separation keeps the UI cleaner and easier to maintain.

8)References
Android Developer documentation (Room, Compose, ViewModel, DataStore).
Some community resources for understanding Room queries.
ChatGPT – used for debugging, explanations, and general guidance.
