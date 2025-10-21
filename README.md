# 🍽️ ShowOff — Recipe Discovery App

A modern, modular Android app for discovering meals and recipes.  
ShowOff is built with **Clean Architecture**, **MVVM**, and a **multi-module** project layout. It offers fast search and navigation between categories, lists, and detailed recipe screens — all implemented with **Jetpack Compose** and reactive Kotlin tooling.

---

## 🚩 Quick highlights

- Multi-module Clean Architecture (core / domain / feature)
- Compose + Material 3 UI
- Kotlin coroutines + Flow for reactive state
- Retrofit + OkHttp for networking, Coil for images
- Koin for dependency injection
- Offline-friendly caching and clear separation between API / cache / UI

---

## 📱 App features

- **Category Browser** — browse meal categories (e.g., Breakfast, Seafood)
- **Recipe Lists** — view recipes for a selected category
- **Recipe Details** — full recipe view (ingredients, instructions, images)
- **Offline / Cache support** — quicker experience and basic offline access
- **Smooth, Reactive UI** — unidirectional data flow (ViewModel → UI)
- **Modular codebase** — easier to maintain, test, and scale

---

## 🏗 Architecture

ShowOff follows Clean Architecture principles with distinct Presentation / Domain / Data layers.

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ PRESENTATION    │     │ DOMAIN          │     │ DATA            │
│ • ViewModels    │ ───▶│ • Entities      │◀─── │ • Repositories  │
│ • Composables   │     │ • UseCases      │     │ • DataSources   │
│ • UI State      │     │ • Interfaces    │     │ • Mappers       │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```


### Patterns & principles
- **Separation of concerns** (Clean Architecture)
- **MVVM** for lifecycle-aware UI
- **Unidirectional data flow / MVI-inspired state handling**
- **Repository pattern** to abstract data sources
- **Dependency injection via Koin**
- **Multi-module** organization for compile-time and organizational benefits

---

## 📦 Project modules (top-level)
```
app/ # Application module (entry point)
core/
├─ core-common/ # Shared utilities, extensions, Result wrappers
├─ core-network/ # Retrofit/OkHttp configuration, interceptors
└─ core-ui/ # Design system, themes, common composables
domain/
└─ meal-domain/ # Domain entities, use-cases, repository interfaces
feature/
├─ category-list/ # Feature: show categories
├─ recipe-by-category-list/ # Feature: list recipes by category
└─ recipe-by-id/ # Feature: recipe details
config/ # Build / CI configuration
```


---

## 🛠 Tech stack

### Languages & platform
- **Kotlin** (project uses Kotlin `2.2.20`)
- **Android (SDK)** — min/target SDK configured in app module

### UI / Architecture
- **Jetpack Compose** (Material 3)
- **Navigation Compose**
- **ViewModel**, **StateFlow** / **SharedFlow**

### Networking & data
- **Retrofit** + **OkHttp**
- **Gson** for JSON parsing (or configured converter)
- **Coil** for image loading
- **Room** (if present — check `core` / `core-database` for local DB module)

### Concurrency & DI
- **Kotlin Coroutines & Flow**
- **Koin** for dependency injection

### Testing (examples)
- **JUnit**
- **MockK** (for mocking in unit tests)

> Note: library versions are defined in `gradle/libs.versions.toml` in the repository. Use that file for exact version pins.

---

## 🧭 Getting started (developer)

### Prerequisites
- Android Studio (latest stable recommended)
- Java 11+
- Kotlin `2.2.20` (configured in `gradle/libs.versions.toml`)
- Android SDK & emulator / device

### Local setup
1. **Clone repository**
   ```bash
   git clone https://github.com/yourusername/showoff.git
   cd showoff
   ```
2. **Add API key** (if required)
   If the project requires a remote API, add your key to `local.properties`:
   ```bash
   API_KEY=YOUR_API_KEY_HERE
   ```
3. **Build & run**
   ```bash
   ./gradlew clean build
   ./gradlew :app:installDebug
   ```
4. Run from **Android Studio**
   Open the project in Android Studio, let Gradle sync, then run the app configuration on a device or emulator.
5. 🧪 **Tests**
   
   5.1. Unit tests live inside each module's `src/test` directory.
   
   5.2. Run all tests
   ```bash
   ./gradlew testDebug
   ```
---
## ✅ Why this structure helps
1. Faster builds: modules compile independently and enable incremental compilation.
2. Clear responsibilities: domain logic is isolated and reusable.
3. Easier testing: test each module independently with mocked interfaces.
4. Team-friendly: teams can own modules/features without stepping on each other’s code.

---
## 🏛️ Multi-Module Benefits

### 🚀 Build Performance
- **Parallel Compilation**: Modules compile independently
- **Incremental Builds**: Only changed modules rebuild
- **Faster CI/CD**: Reduced build times in continuous integration

### 🎯 Scalability
- **Team Collaboration**: Multiple teams can work on different modules
- **Feature Isolation**: New features are self-contained modules
- **Easy Maintenance**: Clear separation of concerns

### 🧪 Testability
- **Independent Testing**: Each module can be tested in isolation
- **Faster Unit Tests**: Test only the relevant module
- **Better Mocking**: Clean interfaces between modules

### 🔄 Reusability
- **Cross-Project**: Core modules can be reused in other projects
- **Platform Sharing**: Domain layer can be shared with KMM
- **Component Library**: UI components become reusable   

---
## 🎨 UI/UX Features

- **🌙 Material 3 Design**: Modern design system with dynamic colors
- **📱 Responsive Layout**: Adapts to different screen sizes
- **🔄 Loading States**: Elegant loading indicators
- **❌ Error Handling**: User-friendly error messages
- **🖼️ Image Loading**: Optimized image loading with Coil


---
## 📷 App Preview
[Screen_recording_20251021_130315.webm](https://github.com/user-attachments/assets/28718fec-9ce3-4375-b707-47a381540a26)


