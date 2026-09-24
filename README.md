# SmartStep

SmartStep is a modern, feature-rich pedometer and health tracking app for Android, built with the latest technologies including Jetpack Compose, Google Generative AI (Gemini Flash Lite), Hardware Step Counter Sensor, Foreground Service, WorkManager, and Koin.

>[!IMPORTANT]
>This project was developed for the **Mobile Dev Campus**, focusing on industry-standard Clean Architecture and modern Android development practices.

## ✨ Features

- **Real-Time Step Tracking**: Accurately tracks daily steps using Android's hardware `TYPE_STEP_COUNTER` sensor with device reboot detection and automatic step accumulation.
- **Background Tracking with Foreground Service**: Seamlessly transitions to a Health Foreground Service with an interactive custom notification (`RemoteViews`) when backgrounded, showing live step counts, calories burned, and goal progress.
- **AI Walking Coach (Gemini AI)**: Integrated with Google Gemini Flash Lite via the Google Generative AI SDK. Delivers motivational tips on the dashboard and features an interactive AI Coach chat for customized walking guidance, habit advice, and fitness encouragement based on live activity metrics.
- **Comprehensive Reports & Analytics**: Track weekly step trends, calories burned, active duration, and distance. Easily switch between metrics and browse past weeks with daily completion badges.
- **Goal Setting & Step History Management**: Customize daily step targets, manually edit past daily step records with an integrated DatePicker, or reset today's steps directly from the navigation drawer.
- **Personalized Metrics**: Configure user profile (gender, height, weight) with support for metric and imperial units (cm, ft/in, kg, lbs) to accurately calculate calories burned and distance covered.
- **Reliable Midnight Settlement**: Dual-layer scheduling strategy combining Android `AlarmManager` (Exact Alarm at 00:01) with `WorkManager` periodic fallback to guarantee daily data archiving and reset across device restarts (`BOOT_COMPLETED`).
- **Modern Material 3 UI**: Clean, responsive, and intuitive interface designed with Jetpack Compose Material 3, including custom wheel pickers, modal bottom sheets, and edge-to-edge layout.

## 🛠 Tech Stack

The project leverages a modern Android tech stack:

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Dependency Injection**: [Koin](https://insert-koin.io/) (Android, Compose, WorkManager)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Key-Value Storage**: [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)
- **AI / LLM Integration**: [Google Generative AI SDK](https://ai.google.dev/) (Gemini Flash Lite)
- **Background Processing**: [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) & Foreground Service (`health` type)
- **Hardware Integration**: Android Sensor API (Step Counter) & AlarmManager
- **Navigation**: [Navigation Compose](https://developer.android.com/guide/navigation/navigation-compose) (Type-Safe Navigation with Kotlinx Serialization)
- **Permissions**: [Accompanist Permissions](https://google.github.io/accompanist/permissions/)
- **Architecture**: Clean Architecture (Presentation, Domain, Data) with MVI / UDF pattern.

## 🏗 Architecture

This app follows **Clean Architecture** principles to ensure separation of concerns and testability:

- **Presentation Layer**: Contains UI components (Jetpack Compose), ViewModels, and UI State/Action/Event following Unidirectional Data Flow (MVI).
- **Domain Layer**: Contains Business Models, Use Cases (`GetStepMetricsUseCase`), Unit Calculators (`UnitCalculator`), and Repository interfaces. Pure Kotlin, no Android dependencies.
- **Data Layer**: Contains Repository implementations, Data Sources (Room, DataStore), Sensor management (`StepsSensorManager`), Gemini AI integration (`GeminiAICoach`), Background Workers (`SaveDailyStepsWorker`), and Foreground Services.

## 🚀 Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/encorex32268/SmartStep.git
   ```

2. **Configure Gemini API Key**:
   Add your Google Gemini API key to your `local.properties` file in the project root:
   ```properties
   GEMINI_API_KEY=your_gemini_api_key_here
   ```

3. **Build and Run**:
   Open the project in Android Studio (Ladybug or later) and run it on an Android device or emulator (Android 7.0+ / API 24+ supported, physical device recommended for step sensor testing).

## 📸 Screenshots

<!-- Screenshots will be added here -->

## 📜 License

This project is open source.

**Author**: [LiHan](https://github.com/encorex32268)

**Project Link**: [SmartStep](https://github.com/encorex32268/SmartStep)

## 🚀 Future Roadmap

- **Health Connect & Wear OS Integration**: Sync walking and activity data seamlessly with Google Health Connect and Wear OS smartwatches.
- **Enhanced AI Coaching**: Expand AI coach capabilities with voice interaction, weekly activity trend analysis, and personalized long-term milestone coaching.
- **Achievements & Social Sharing**: Add milestone badges, streak rewards, and customizable summary cards to share progress on social media.
- **Unit & UI Testing**: Increase code coverage with **Room database tests**, **Worker tests**, and **Compose UI tests** to ensure tracking reliability and app stability.
