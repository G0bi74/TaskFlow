# TaskFlow - Aplikacja do Zarządzania Zadaniami

> **Uwaga:** Ten projekt został stworzony jako zadanie mające na celu rozwinięcie umiejętności programowania w zakresie Android Development, Kotlin oraz Firebase.

## 📱 O Projekcie

TaskFlow (dawniej "apka") to aplikacja mobilna na Androida do zarządzania listą zadań (To-Do List) z zaawansowanymi funkcjami organizacji i priorytetyzacji zadań. Aplikacja została zbudowana przy użyciu nowoczesnych technologii Android, w tym Jetpack Compose oraz integracji z Firebase.

## ✨ Funkcje

- **Zarządzanie zadaniami:** Tworzenie, edytowanie i usuwanie zadań
- **Kategorie zadań:** 
  - Zadania oczekujące (Pending Tasks)
  - Zadania ukończone (Completed Tasks)
- **Priorytetyzacja:** System priorytetów dla zadań
- **Terminy:** Możliwość ustawiania deadline'ów dla zadań
- **Uwierzytelnianie:** System logowania i rejestracji użytkowników przy użyciu Firebase Authentication
- **Synchronizacja w chmurze:** Dane zadań przechowywane w Firebase Firestore
- **Nowoczesny interfejs:** Interfejs użytkownika zbudowany w Jetpack Compose z Material Design 3

## 🛠️ Technologie

- **Język programowania:** Kotlin
- **Framework UI:** Jetpack Compose
- **Architektura:** MVVM (Model-View-ViewModel)
- **Backend:** Firebase
  - Firebase Authentication - uwierzytelnianie użytkowników
  - Firebase Firestore - baza danych NoSQL w chmurze
  - Firebase Realtime Database
- **Nawigacja:** Navigation Compose
- **Build System:** Gradle Kotlin DSL
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)

## 📂 Struktura Projektu

```
app/src/main/java/pl/g0bi74/todolist/
├── MainActivity.kt              # Główna aktywność aplikacji
├── MainViewModel.kt            # ViewModel zarządzający stanem aplikacji
├── DownloadLogic.kt            # Logika pobierania danych
├── components/
│   └── TaskItem.kt             # Komponent UI dla pojedynczego zadania
├── screens/
│   ├── MainScreen.kt           # Główny ekran aplikacji
│   ├── AuthenticationScreen.kt # Ekran logowania/rejestracji
│   ├── PendingTasksScreen.kt  # Ekran zadań oczekujących
│   └── CompletedTasksScreen.kt # Ekran zadań ukończonych
├── nav/
│   └── Navigation.kt           # Konfiguracja nawigacji
└── ui/theme/                   # Motywy i style aplikacji
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

## 📋 Wymagania

- Android Studio (2023.1.1 lub nowszy)
- JDK 17 lub nowszy
- Konto Firebase z skonfigurowanym projektem
- Minimalna wersja Android: 8.0 (API 26)

## 🚀 Instalacja i Konfiguracja

1. **Sklonuj repozytorium:**
   ```bash
   git clone https://github.com/G0bi74/apka.git
   cd apka
   ```

2. **Skonfiguruj Firebase:**
   - Utwórz projekt w [Firebase Console](https://console.firebase.google.com/)
   - Dodaj aplikację Android do projektu Firebase
   - Pobierz plik `google-services.json` i umieść go w katalogu `app/`
   - Włącz Firebase Authentication i Firestore w konsoli Firebase

3. **Otwórz projekt w Android Studio:**
   - File → Open → wybierz folder projektu

4. **Synchronizuj projekt z plikami Gradle:**
   - Android Studio automatycznie zsynchronizuje zależności

5. **Uruchom aplikację:**
   - Wybierz emulator lub podłącz fizyczne urządzenie Android
   - Kliknij "Run" (▶️)

## 💡 Użytkowanie

1. **Rejestracja/Logowanie:** 
   - Przy pierwszym uruchomieniu zarejestruj nowe konto lub zaloguj się przy użyciu istniejących danych

2. **Dodawanie zadań:**
   - Wypełnij formularz na głównym ekranie (tytuł, opis, deadline, priorytet)
   - Kliknij przycisk dodawania zadania

3. **Zarządzanie zadaniami:**
   - Oznaczaj zadania jako ukończone
   - Przeglądaj zadania oczekujące i ukończone w osobnych widokach
   - Usuwaj nieaktualne zadania

4. **Wylogowanie:**
   - Użyj opcji wylogowania dostępnej w aplikacji

## 🎓 Cele Edukacyjne

Ten projekt został stworzony jako część procesu nauki i rozwoju umiejętności programowania. Główne obszary rozwoju to:

- **Kotlin:** Praktyczne zastosowanie języka Kotlin w rozwoju aplikacji mobilnych
- **Jetpack Compose:** Nauka deklaratywnego podejścia do budowania UI
- **MVVM Architecture:** Implementacja wzorca architektonicznego
- **Firebase Integration:** Integracja z usługami backendowymi w chmurze
- **Android Development:** Zrozumienie cyklu życia aplikacji i komponentów Androida
- **Material Design:** Implementacja wytycznych Material Design 3
- **Asynchroniczne programowanie:** Praca z Kotlin Coroutines i Flow

## 🔄 Sugestia Nowej Nazwy Repozytorium

Obecna nazwa "apka" (pol. "aplikacja") jest bardzo ogólna. Sugerowane lepsze nazwy:

1. **TaskFlow** - odzwierciedla płynność zarządzania zadaniami
2. **FireTask** - podkreśla użycie Firebase
3. **KotlinToDo** - wskazuje na technologię i cel aplikacji
4. **ComposeTaskManager** - odzwierciedla użycie Jetpack Compose
5. **TodoList-Compose-Firebase** - opisowa nazwa technologii

**Rekomendacja:** `TaskFlow` - krótka, profesjonalna i oddająca charakter aplikacji.

## 📝 Licencja

Projekt edukacyjny stworzony do celów nauki i rozwoju umiejętności programowania.

## 👤 Autor

G0bi74

---

## English Version

# TaskFlow - Task Management Application

> **Note:** This project was created as an assignment aimed at developing programming skills in Android Development, Kotlin, and Firebase.

## 📱 About the Project

TaskFlow (formerly "apka") is an Android mobile application for managing a to-do list with advanced task organization and prioritization features. The application is built using modern Android technologies, including Jetpack Compose and Firebase integration.

## ✨ Features

- **Task Management:** Create, edit, and delete tasks
- **Task Categories:**
  - Pending Tasks
  - Completed Tasks
- **Prioritization:** Priority system for tasks
- **Deadlines:** Ability to set deadlines for tasks
- **Authentication:** User login and registration system using Firebase Authentication
- **Cloud Synchronization:** Task data stored in Firebase Firestore
- **Modern UI:** User interface built with Jetpack Compose and Material Design 3

## 🛠️ Technologies

- **Programming Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **Backend:** Firebase
  - Firebase Authentication - user authentication
  - Firebase Firestore - cloud NoSQL database
  - Firebase Realtime Database
- **Navigation:** Navigation Compose
- **Build System:** Gradle Kotlin DSL
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)

## 🚀 Installation and Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/G0bi74/apka.git
   cd apka
   ```

2. **Configure Firebase:**
   - Create a project in [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app to your Firebase project
   - Download the `google-services.json` file and place it in the `app/` directory
   - Enable Firebase Authentication and Firestore in the Firebase console

3. **Open the project in Android Studio**

4. **Sync project with Gradle files**

5. **Run the application**

## 🎓 Learning Objectives

This project was created as part of the learning process and skill development. Main areas of development include:

- **Kotlin:** Practical application of Kotlin in mobile app development
- **Jetpack Compose:** Learning declarative UI building
- **MVVM Architecture:** Implementation of architectural patterns
- **Firebase Integration:** Integration with cloud backend services
- **Android Development:** Understanding Android app lifecycle and components
- **Material Design:** Implementation of Material Design 3 guidelines
- **Asynchronous Programming:** Working with Kotlin Coroutines and Flow

## 🔄 Suggested New Repository Name

The current name "apka" (Polish for "app") is very generic. Suggested better names:

**Recommendation:** `TaskFlow` - short, professional, and reflects the app's purpose.

## 👤 Author

G0bi74
