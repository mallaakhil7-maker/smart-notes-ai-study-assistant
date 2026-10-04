# Smart Notes – AI Study Assistant

A modern Android app for students to scan handwritten or printed notes, extract OCR text, organize it into subjects, and use AI-powered summarization for study support.

## Features in MVP

- Home dashboard with quick actions
- Camera or gallery note scanning
- OCR text extraction with ML Kit
- Editable extracted text before save
- Local note storage with Room
- Subject organization
- Search notes by title, subject, tags, and content
- AI summarization using OpenAI-compatible API
- Dark mode and mobile-friendly Material 3 design
- Privacy messaging before AI calls

## Tech stack

- Kotlin
- Jetpack Compose
- Material 3
- Room Database
- CameraX / Activity Results
- Google ML Kit Text Recognition
- OkHttp + JSON

## Prerequisites

- Android Studio Jellyfish or newer
- Android SDK 34
- JDK 17

## API key setup

1. Create a file named `local.properties` in the project root.
2. Add the following line:

   OPENAI_API_KEY=your_key_here

3. Save the file and sync Gradle.

Note: If the key is absent, the app still works offline for note creation and OCR, but AI summarization will show a clear configuration message instead of sending content to any service.

## Run locally

1. Open the project in Android Studio.
2. Let Gradle sync.
3. Select an Android emulator or connected device.
4. Run the app.

## Privacy notice

- Notes and images are stored locally on the device by default.
- AI features only send note text to the configured AI service when the user explicitly taps a summarization action.
- The app clearly indicates when internet access is required for AI features.

## Future roadmap

After the MVP, the app can be extended with:

- flashcards
- quizzes and scoring
- AI chat over a note
- accounts and cloud sync
- advanced subject analytics
