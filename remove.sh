#!/bin/bash

echo "🧹 Cleaning up ft_hangouts environment..."

# 1. Run Gradle clean to safely remove build outputs
if [ -f "gradlew" ]; then
    echo "Running Gradle clean..."
    ./gradlew clean --no-daemon
else
    echo "⚠️ gradlew not found, skipping Gradle clean."
fi

# 2. Remove IDE and local configuration files
echo "🗑️ Removing build directories, caches, and local configurations..."

# Remove IDE specific files
rm -rf .idea/
rm -rf *.iml
rm -rf app/*.iml

# Remove Gradle caches and local files
rm -rf .gradle/
rm -rf local.properties

# Force remove any lingering build folders just in case
rm -rf build/
rm -rf app/build/

echo "✨ Cleanup complete! The repository is now clean."