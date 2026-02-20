# ft_hangouts

An Android application for managing contacts, built as part of the 42 school curriculum.

## Features
- View a list of contacts
- Add new contacts
- Store contacts persistently using SQLite
- Built with Kotlin and the Android SDK

## Project Structure
- `MainActivity.kt`: Main entry point displaying the contact list.
- `AddContactActivity.kt`: Activity to add a new contact.
- `Contact.kt`: Data model for a contact.
- `DatabaseHelper.kt`: SQLite database helper for persistent storage.

## Getting Started

### Prerequisites
- Java Development Kit (JDK 17 recommended)
- Android SDK

### Setup
To set up the project dependencies and configure the Android SDK path automatically, run the provided setup script:
```bash
./setup.sh
```

### Building and Running
You can build the project using the provided Gradle wrapper:
```bash
./gradlew assembleDebug
```
To install the app on a connected device or emulator:
```bash
./gradlew installDebug
```

### Cleanup
To clean the project, remove build directories, caches, and local configurations, run:
```bash
./remove.sh
```