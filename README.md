# HMS Admin


HMS Admin is an Android application designed for administrators of a Hostel Management System. It provides a mobile interface to manage essential hostel operations, including rooms, students, and attendance, by communicating with a backend server.

## Features

*   **Admin Authentication:** Secure login for administrators.
*   **Dashboard:** A central home screen for easy navigation to all management modules.
*   **Room Management:**
    *   Add new rooms, specifying the type (e.g., General, Bedroom).
    *   View a list of all existing rooms.
    *   Update or delete room details.
*   **Bed Management:**
    *   For rooms of type "Bedroom", administrators can add, update, or remove individual beds.
*   **Student Management:**
    *   Add new students and assign them to available rooms and beds.
    *   View a comprehensive list of all students.
    *   Update student information or delete student records.
    *   Remove room assignments from students.
*   **Attendance Tracking:**
    *   View student attendance records.
    *   Filter attendance data by date, room number, or specific student name.

## How It Works

The application serves as a client interface that interacts with a remote backend service. All data operations (adding, updating, fetching, deleting) are performed by making HTTP POST requests to a RESTful API endpoint. The app uses `AsyncTask` to handle these network operations asynchronously, ensuring a smooth user experience.

1.  **Login:** The administrator logs in with their credentials. A successful login is stored in `SharedPreferences` to maintain the session.
2.  **Home Screen:** The admin is presented with a dashboard to choose between managing rooms, students, or viewing attendance.
3.  **Manage Rooms:** Admins can view all rooms. They can select a room to see its details, update it, or delete it. For bedrooms, there's an option to manage the associated beds. New rooms can be added via a dedicated form.
4.  **Manage Students:** Admins can view a list of all students, add new ones, or select an existing student to view/edit their details. Student details include personal information and room/bed assignment.
5.  **View Attendance:** The attendance screen allows admins to check attendance records. It defaults to the current day but allows date selection. The records can be filtered to narrow down the results by a specific room or student.

## Technical Stack

*   **Platform:** Android
*   **Language:** Java
*   **Build Tool:** Gradle
*   **Backend Communication:** REST API over HTTP (using HttpURLConnection)
*   **Data Format:** JSON
*   **Asynchronous Processing:** `AsyncTask`
*   **UI Components:** `RecyclerView`, `CardView`, `Spinner`, `FloatingActionButton`

## Setup and Installation

1.  **Clone the Repository:**
    ```sh
    git clone https://github.com/Katukuri-vamshidharReddy/HMS-ADMIN.git
    ```
2.  **Open in Android Studio:**
    *   Launch Android Studio.
    *   Select "Open an existing Android Studio project".
    *   Navigate to the cloned repository folder and open it.
3.  **Sync Dependencies:**
    *   Allow Gradle to sync and download all the required project dependencies.
4.  **Backend Configuration:**
    *   The application is configured to connect to a backend service at `http://ahostel.hostoise.com/Handler1.ashx` as defined in `app/src/main/java/com/example/hmsadmin/RestAPI.java`. This backend must be running and accessible for the app to function correctly.
5.  **Build and Run:**
    *   Build the project and run it on an Android emulator or a physical device.

## Code Structure

The project follows a standard Android application structure.

```
app/src/main/java/com/example/hmsadmin/
├── Activities        # UI screens like LoginActivity, HomeActivity, MainActivity
├── Adapters          # Connects data to RecyclerViews (e.g., AllRoomAdapter, StudentAdapter)
├── Models            # POJO classes for data (e.g., AllRooms, Students, Beds)
├── RestAPI.java      # Handles all communications with the backend API
└── JSONParse.java    # Helper class to parse JSON responses
