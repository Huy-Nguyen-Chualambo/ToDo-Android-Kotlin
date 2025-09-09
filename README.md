# ToDo App - Android Kotlin

A modern, feature-rich ToDo application built with Kotlin and Android Architecture Components. This app helps you manage your daily tasks with an intuitive Material Design interface.

## ✨ Features

- **Task Management**
  - ✅ Add new tasks with title and description
  - ✏️ Edit existing tasks
  - 🗑️ Delete tasks
  - ☑️ Mark tasks as complete/incomplete with visual strikethrough

- **Deadline Management**
  - 📅 Set deadlines with date and time picker
  - 🚨 Visual indicators for overdue tasks (red text)
  - ⚠️ Warning indicators for tasks due soon (yellow text)

- **Organization & Search**
  - 🔍 Search tasks by title or description
  - 📂 Filter tasks: All, Active, Completed
  - 📋 Sort by creation date or deadline

- **User Experience**
  - 🎨 Material Design 3 interface
  - ✨ Smooth animations for list items
  - 📱 Responsive design
  - 🌙 Dark/Light theme support

## 📱 Screenshots

*Note: Screenshots will be added soon to showcase the app's interface and features.*

## 🛠️ Technology Stack

### Core Technologies
- **Kotlin** - Modern programming language for Android
- **Android SDK** - Target SDK 35, Min SDK 24

### Architecture Components
- **MVVM Pattern** - Model-View-ViewModel architecture
- **Room Database** - Local data persistence
- **LiveData** - Observable data holder
- **ViewModel** - UI-related data holder
- **View Binding** - Type-safe view references
- **Data Binding** - Declarative UI programming

### UI & UX
- **Material Design Components** - Modern UI components
- **RecyclerView** - Efficient list display
- **Animations** - Smooth user interactions
- **ConstraintLayout** - Flexible UI layouts

### Asynchronous Programming
- **Kotlin Coroutines** - Asynchronous programming
- **Flow** - Reactive data streams

## 🏗️ Architecture Overview

This app follows the **MVVM (Model-View-ViewModel)** architecture pattern:

```
├── UI Layer (Activities, Fragments, Adapters)
│   ├── MainActivity
│   └── TodoAdapter
│
├── ViewModel Layer
│   └── TodoViewModel
│
├── Repository/Data Layer
│   ├── Room Database
│   ├── TodoDao
│   ├── TodoDatabase
│   └── Todo Entity
│
└── Model Layer
    ├── Todo (Data Class)
    ├── TodoFilter (Enum)
    └── Converters
```

### Key Components

- **MainActivity**: Main UI controller handling user interactions
- **TodoViewModel**: Manages UI data and business logic
- **Room Database**: Local SQLite database with type converters
- **TodoAdapter**: RecyclerView adapter with DiffUtil for efficient updates

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK with API level 24 or higher
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Huy-Nguyen-Chualambo/ToDo-Android-Kotlin.git
   cd ToDo-Android-Kotlin
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Click "Open an existing Android Studio project"
   - Navigate to the cloned directory and select it

3. **Sync the project**
   - Android Studio will automatically sync Gradle files
   - Wait for the sync to complete

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button (▶️) or press `Shift + F10`

### Build Commands

```bash
# Build the project
./gradlew build

# Install debug APK
./gradlew installDebug

# Run tests
./gradlew test
```

## 📖 Usage

### Adding a New Task
1. Tap the floating action button (➕)
2. Enter task title (required) and description (optional)
3. Optionally set a deadline using the date/time picker
4. Tap "Add" to save the task

### Managing Tasks
- **Complete/Uncomplete**: Tap the checkbox next to any task
- **Edit**: Tap on the task item to open the edit dialog
- **Delete**: Tap the delete button (🗑️) on any task

### Filtering and Search
- Use the search bar to find tasks by title or description
- Use filter chips: "All", "Active", or "Completed"
- Tasks are sorted by creation date by default

## 📁 Project Structure

```
app/src/main/
├── java/com/example/todoapp/
│   ├── MainActivity.kt              # Main Activity
│   ├── data/
│   │   ├── Todo.kt                  # Todo entity
│   │   ├── TodoDao.kt               # Data Access Object
│   │   ├── TodoDatabase.kt          # Room database
│   │   ├── TodoFilter.kt            # Filter enum
│   │   └── Converters.kt            # Type converters
│   ├── ui/
│   │   └── TodoAdapter.kt           # RecyclerView adapter
│   └── viewmodel/
│       └── TodoViewModel.kt         # ViewModel
├── res/
│   ├── layout/                      # XML layouts
│   ├── values/                      # Strings, colors, themes
│   ├── drawable/                    # Icons and drawables
│   └── anim/                        # Animations
└── AndroidManifest.xml
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. Here's how you can contribute:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/AmazingFeature
   ```
5. **Open a Pull Request**

### Development Guidelines

- Follow Kotlin coding conventions
- Use meaningful commit messages
- Add comments for complex logic
- Ensure the app builds without warnings
- Test your changes on different screen sizes

## 🐛 Known Issues

- None at the moment. Please report any bugs you find!

## 🔮 Future Enhancements

- [ ] Task categories and tags
- [ ] Recurring tasks
- [ ] Task prioritization
- [ ] Data export/import
- [ ] Cloud synchronization
- [ ] Task sharing
- [ ] Voice input for tasks
- [ ] Widget support

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

**Huy Nguyen Chualambo**
- GitHub: [@Huy-Nguyen-Chualambo](https://github.com/Huy-Nguyen-Chualambo)

## 🙏 Acknowledgments

- Thanks to the Android development community
- Material Design guidelines by Google
- Android Architecture Components documentation

---

⭐ **Star this repository if you find it helpful!**