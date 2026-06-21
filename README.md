# FastCash - KMP Payment App

A cross-platform FinTech application built with Kotlin Multiplatform (KMP), Firebase, and Jetpack Compose.

## Architecture
This project follows a Clean Architecture approach within a KMP structure:
- **sharedLogic**: Contains domain models, validation logic, Ktor API client, and platform-specific Firestore repositories (using expect/actual).
- **sharedUI**: Shared UI components built with Compose Multiplatform.
- **androidApp**: Android entry point, Koin initialization, and platform-specific configuration.

## Features
- **Send Payment**: Form with validation for email, amount, and currency.
- **Transaction History**: Real-time listing of transactions fetched from Firebase Firestore.
- **Cross-Platform Logic**: 100% of validation and 90% of data fetching code is shared.

## Tech Stack
- **UI**: Jetpack Compose / Compose Multiplatform
- **DI**: Koin
- **Networking**: Ktor
- **Database**: Firebase Firestore
- **Serialization**: kotlinx-serialization

## Setup Instructions

### 1. Firebase Setup
- Create a Firebase project at [Firebase Console](https://console.firebase.google.com/).
- Add an Android app with package name `com.kelly.fastcash`.
- Download `google-services.json` and place it in the `androidApp/` directory.
- Enable **Cloud Firestore** in your Firebase project.

### 2. Mock API
- The app calls `POST /payments`.
- You can use a mock server (e.g., [Mocki](https://mocki.io/) or [JSON Server](https://github.com/typicode/json-server)) to host the endpoint.
- Update the `baseUrl` in `sharedLogic/src/commonMain/kotlin/com/kelly/fastcash/data/remote/PaymentApi.kt`.

### 3. Running the App
- Open in Android Studio.
- Sync Gradle.
- Run the `androidApp` configuration.

## Running Tests

### Unit Tests
Run the shared logic unit tests:
```bash
./gradlew :sharedLogic:test
```

### BDD Tests
The scenarios are defined in `sharedLogic/src/commonTest/resources/features/payments.feature`. You can run them using the Cucumber runner (if configured) or view the logic mirrored in `PaymentValidatorTest.kt`.

### API Performance (JMeter)
1. Open the provided `payment_test.jmx` (template below) in JMeter.
2. Set the target URL to your `/payments` endpoint.
3. Run the thread group with 5 concurrent users.

### UI Testing (Appium)
- Appium scripts should target the `MainActivity`.
- Use the `contentDescription` "Send Payment Button" to locate interactions.

## KMP Potential
The shared logic is already prepared for iOS. By implementing the `actual` FirestoreRepository for iOS using the Firebase iOS SDK, the entire business logic and UI (via Compose Multiplatform) can be ported to iOS with minimal effort.
