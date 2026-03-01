# ft_hangouts

An Android application for managing contacts, built as part of the 42 school curriculum.

## Features
- View a list of contacts
- Add new contacts
- Edit and delete existing contacts
- Send and receive SMS messages
- Store contacts and messages persistently using SQLite
- Localized in English and Simplified Chinese
- Lifecycle tracking with background timestamp logging
- Built with Kotlin and the Android SDK

## Project Structure
- `MainActivity.kt`: Main entry point displaying the contact list.
- `AddContactActivity.kt`: Activity to add a new contact.
- `ViewContactActivity.kt`: Activity to view and manage contact details.
- `MessageActivity.kt`: Interface for sending and viewing SMS messages.
- `SmsReceiver.kt`: Broadcast receiver for handling incoming SMS.
- `BaseActivity.kt`: Base class handling common lifecycle events.
- `Contact.kt` & `Message.kt`: Data models.
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

### Build Process

The project uses Gradle for building. Make sure you have run the setup script first.

**Debug Build**
To compile the application in debug mode:
```bash
./gradlew assembleDebug
```
The output APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`

**Install on Device**
To build and immediately install on a connected Android device or emulator:
```bash
./gradlew installDebug
```

**Release Build**
To compile the application in release mode (requires signing configuration setup):
```bash
./gradlew assembleRelease
```

### Cleanup
To clean the project, remove build directories, caches, and local configurations, run:
```bash
./remove.sh
```