# FastCash - KMP Payment App

A cross-platform FinTech application built with **Kotlin Multiplatform (KMP)**, **Firebase**, and **Jetpack Compose**. This project demonstrates a robust architecture for sending payments and tracking transaction history.

## 🚀 Architecture
This project follows **Clean Architecture** principles within a KMP structure:

- **`:sharedLogic`**: Core business logic (Common to all platforms).
    - **Domain**: Entities (`PaymentRequest`, `PaymentResponse`), Use Cases (`ProcessPaymentUseCase`, `GetTransactionsUseCase`), and Repository interfaces (`PaymentRepository`, `DatabaseRepository`).
    - **Data**: Repository implementations, DTOs, Mappers, and **DataSource abstractions**.
    - **DI**: Koin modules for common and platform-specific dependencies.
- **`:sharedUI`**: Shared UI components built with **Compose Multiplatform**.
    - Includes the main `PaymentFormScreen` and `TransactionHistory` (integrated in a single reactive flow).
    - Managed via `PaymentViewModel` in the presentation layer.
- **`:androidApp`**: Android-specific entry point and configuration.
- **`:appiumTests`**: Dedicated module for Appium UI automation.

## ✨ Features
- **Send Payment**:
    - Input validation for email, amount (>0), and currency (USD, EUR).
    - Calls a REST API for processing.
    - Real-time sync to **Firebase Firestore** via the `DatabaseRepository`.
- **Transaction History**:
    - Displays a real-time list of transactions using Firestore's snapshot listeners.
    - Each record shows the recipient, amount, currency, status, and formatted timestamp.

## 🛠 Tech Stack
- **UI**: Jetpack Compose / Compose Multiplatform
- **DI**: Koin
- **Networking**: Ktor (with Content Negotiation & Logging)
- **Database**: Firebase Firestore (Android-native via `DataSource` abstraction)
- **Serialization**: `kotlinx-serialization`
- **Date/Time**: `kotlinx-datetime`

## 📦 Setup Instructions

### 1. Firebase Configuration
1.  Create a Firebase project at [Firebase Console](https://console.firebase.google.com/).
2.  Add an Android app with package name `com.kelly.fastcash`.
3.  Download `google-services.json` and place it in the `androidApp/` directory.
4.  Enable **Cloud Firestore** and create a collection named `payments`.

### 2. Backend API
The app is configured to use a mock API: `https://6a36bb11766b831960f9816a.mockapi.io`.
You can update the `baseUrl` in `sharedLogic/src/commonMain/kotlin/com/kelly/fastcash/data/remote/PaymentService.kt` if you wish to use a different endpoint.

### 3. Running the App
1.  Open the project in **Android Studio**.
2.  Sync Gradle.
3.  Run the `androidApp` configuration on an emulator or physical device.

## 🧪 Testing

### ✅ Unit Tests
Covers Use Case logic and validation.
```bash
./gradlew :sharedLogic:test
```

### 🥒 BDD Tests (Cucumber)
Behavior-driven scenarios verified against the shared logic layer.
- **Scenarios**: `sharedLogic/src/commonTest/resources/features/payments.feature`
- **Execution**: Included in the Android host tests.
```bash
./gradlew :sharedLogic:testAndroidHostTest
```

### 📱 UI Testing (Appium)
Automated flow: Enter details -> Send -> Verify in history list.
**Prerequisites**:
1.  Run Appium Server (`appium`).
2.  Install the app on a connected device/emulator.
**Execution**:
```bash
./gradlew :appiumTests:test
```

### 📈 Performance Testing (JMeter)
The project includes a JMeter test plan to evaluate the API's performance under load, specifically targeting the payment processing endpoint.

- **Test Plan Location**: `jmeter/fast_cash_test_plan.jmx`
- **Configuration**: 5 concurrent users (Thread Group) hitting the `POST /payments` endpoint.
- **Artifacts**:
    - **Raw Data**: `jmeter/fastcashtest.jtl`
    - **Visual Report**: Open `jmeter/html_report/index.html` in any web browser to view the Dashboard Report (includes Response Time Percentiles, Throughput, and Error rates).

**How to run**:
1.  Install [Apache JMeter](https://jmeter.apache.org/download_jmeter.cgi).
2.  Launch JMeter and open the `.jmx` file.
3.  To run and generate a new report via command line:
    ```bash
    jmeter -n -t jmeter/fast_cash_test_plan.jmx -l jmeter/new_results.jtl -e -o jmeter/new_report
    ```

## 🍏 KMP & Cross-Platform Potential
The architecture is designed for multiplatform expansion. 
- **Business Logic**: 100% shared in `sharedLogic`.
- **UI**: 95% shared in `sharedUI`.
- **iOS Support**: Prepared with a `DataSource` abstraction. To enable iOS, simply implement a `IosFirestoreDataSource` in `iosMain` using the Firebase iOS SDK and provide it via Koin.

---