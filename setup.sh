#!/bin/bash

echo "🚀 Setting up ft_hangouts dependencies..."

# 1. Ensure gradlew is executable
if [ -f "gradlew" ]; then
    chmod +x gradlew
    echo "✅ Made gradlew executable."
else
    echo "❌ gradlew not found! Make sure you are in the project root."
    exit 1
fi

# 2. Check for Java presence (Required for Gradle)
if ! command -v java &> /dev/null; then
    echo "⚠️ Java is not installed or not in PATH. Please install JDK 17 (recommended for this setup)."
else
    echo "✅ Java is installed."
fi

# 3. Create local.properties if it doesn't exist (crucial for CLI builds)
if [ ! -f "local.properties" ]; then
    echo "⚠️ local.properties not found. Attempting to generate one based on your OS..."
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # Default macOS Android SDK path
        echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties
        echo "✅ Created local.properties with default macOS SDK path."
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Default Linux Android SDK path
        echo "sdk.dir=$HOME/Android/Sdk" > local.properties
        echo "✅ Created local.properties with default Linux SDK path."
    else
        echo "⚠️ Could not determine OS. Please create local.properties manually with your sdk.dir."
    fi
else
    echo "✅ local.properties already exists."
fi

# 4. Download dependencies using Gradle
echo "📦 Resolving and downloading project dependencies..."
./gradlew dependencies --no-daemon

echo "🎉 Setup complete! You can now open the project in Android Studio or build it via CLI using './gradlew assembleDebug'."