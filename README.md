# MoveMate – Exercise Tracking App

A simple and modern Android fitness tracking app built with **Kotlin**, **Jetpack Compose**, **Room**, and **the Step Counter sensor**.  
<div align="center">
  <img src="https://github.com/user-attachments/assets/5880e09f-ea75-4913-a355-69f52cc54c4a" width="200">
  <img src="https://github.com/user-attachments/assets/651a58bf-ad1b-4641-8e4a-6a0813eea9d6" width="200">
  <img src="https://github.com/user-attachments/assets/bfba14d3-b016-4f8d-9e0e-89f509bebe81" width="200">
</div>


---

##  Features

###  Workout Logging
- Add, edit, and delete workouts  
- Track: type, duration, distance, effort rating, and optional notes  
- Automatic calorie estimation  

###  Workout List (Home Screen)
- Scrollable list of saved workouts  
- Modern **swipe-to-delete** gesture  
- Undo deletion via **Snackbar**  

###  Progress Screen
- Total minutes trained  
- Estimated calories burned  
- Animated weekly goal progress bar (extra UI feature)

###  Step Sensor Integration (Extra Feature)
- Live step counter using **SensorManager**  
- Converts steps → suggested duration  
- “Use steps to suggest duration” button  
- Real hardware integration (requires physical device)

###  Tech Stack
- Kotlin  
- Jetpack Compose  
- Material 3  
- Room (SQLite database)  
- StateFlow + MVVM  
- Navigation Compose  

---

##  Project Structure
    /app
     ├── data
     │    ├── local (Room database, DAO, entities)
     │    └── repository
     ├── sensor (StepCounterDataSource)
     ├── userinterface
     │    ├── components
     │    └── screen
     └── viewmodel (WorkoutViewModel)

---

##  Installation & Running

### Requirements
- Android Studio Ladybug or later  
- Physical Android device (required for step sensor)  
- Min SDK: **28**  
- Target SDK: **36**

### Steps
1. Clone the repository:  
   `git clone <your_repo_here>`
2. Open the project in Android Studio  
3. Connect your Android device via USB  
4. Enable:
   - Developer Options  
   - USB Debugging  
5. Select your device in the Run dropdown  
6. Press **▶ Run**  
7. Walk with your phone to test real step tracking

---

##  Localisation
MoveMate supports:

- **English** → `values/strings.xml`  
- **Spanish** → `values-es/strings.xml`

---

##  References
- Android Developer Docs (Compose, Room, Sensors)  
- Kotlin Documentation – coroutines, StateFlow  
- Material 3 Compose Documentation  

---

##  Conclusion
MoveMate demonstrates:
- CRUD with Room  
- Modern Compose UI  
- Sensor integration  
- Localisation  
- A clean MVVM architecture  



