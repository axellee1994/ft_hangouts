# ft_hangouts

An Android application for managing contacts, built as part of the 42 school curriculum.

## Features
- **Contact Management:** View, add, edit, and delete contacts.
- **SMS Messaging:** Send text messages directly from the app and receive incoming strings from saved contacts.
- **Persistent Storage:** Safely store all contacts and messages using Android's native SQLite Database.
- **App Customization:** Customize the app's header color dynamically from a dropdown menu.
- **Localization:** Full multi-language support covering English and Simplified Chinese (`zh`) depending on the device's system settings.
- **Lifecycle Tracking:** Tracks application backgrounding events to display a "Last seen" timestamp toast upon returning to the app.

## Project Structure
- `MainActivity.kt`: Main entry point displaying the contact list.
- `AddContactActivity.kt`: Activity to add a new contact.
- `ViewContactActivity.kt`: Activity to view and manage specific contact details.
- `MessageActivity.kt`: Interface providing a chat-like view for sending and viewing SMS messages.
- `SmsReceiver.kt`: Broadcast receiver that safely intercepts and processes incoming SMS messages.
- `BaseActivity.kt`: Base class inherited by all activities to handle common overarching logic (like header customization and lifecycle timestamp tracking).
- `Contact.kt` & `Message.kt`: Data models defining schemas.
- `DatabaseHelper.kt`: SQLite handler mediating all persistent read/writes.

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