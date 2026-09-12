# AWOL - Employee Tracker

[![Android Build](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Gradle](https://img.shields.io/badge/Gradle-6.5-blue.svg)](https://gradle.org)
[![AndroidX](https://img.shields.io/badge/AndroidX-Modern-orange.svg)](https://developer.android.com/jetpack/androidx)
[![Architecture](https://img.shields.io/badge/Architecture-Centralized-brightgreen.svg)]()

**AWOL Employee Tracker** is a unified, stabilized Android attendance management system featuring **automated GPS geofencing**, **biometric check-in/out verification**, **dual-role dashboards (Employee & Administrator)**, and **offline-first SQLite caching with sync capabilities**.

Previously, implementations were scattered across disparate subdirectories. The repository has now been centralized into a single root-level Android Studio project with a modern AndroidX architecture, while preserving historical references in [`legacy_references/`](legacy_references/).

---

## Key Features

### 1. 🕒 Smart Clock-In & Shift Tracking
- **Biometric Security**: BiometricPrompt verification (fingerprint or device credentials) for secure clock-in/clock-out actions.
- **Dynamic Shift Timer**: Live stopwatch tracking shift elapsed time and daily accumulated work hours.
- **Instant Status Indicator**: Real-time visual status badges ("Checked In" vs "Checked Out").

### 2. 📍 Automated GPS Geofencing
- **Workplace Proximity Monitoring**: Continuously measures distance from office coordinates using Haversine calculation.
- **Foreground Location Service**: Background location service (`LocationTrackingService`) with persistent status notifications.
- **Configurable Radius**: Workplace coordinates and geofence threshold radius configurable in Settings (default: 100m).

### 3. 👥 Dual Role Portals (Employee & Admin)
- **Employee Portal**:
  - Personal check-in / check-out controls.
  - Daily, weekly, and monthly attendance statistics.
  - 30-day chronological attendance history with sync status.
- **Administrator Portal**:
  - Live workforce headcount (Total Staff, Present Today, Absent).
  - Filter toggle to view all employees or only those currently on the clock.
  - In-app employee onboarding dialog to register new team members.

### 4. 🌓 Modern Material UI & Dynamic Theming
- Native **Light & Dark Mode** engine with dynamic runtime switching.
- Bottom sheet authentication screen with quick one-click demo credentials.
- Clean Material Design cards, badges, and responsive layouts.

### 5. 💾 Offline-First SQLite Architecture
- Embedded SQLite database (`awol_employee_tracker.db`) stores attendance stamps and employee records locally.
- Tracks `is_synced` flags to allow offline clocking in/out with manual or automatic cloud synchronization.

---

## Directory Structure

```text
AWOL-Employee-tracker/
├── app/                                # Primary centralized Android application module
│   ├── src/main/
│   │   ├── AndroidManifest.xml         # Consolidated permissions, services, activities
│   │   ├── java/com/awol/employeetracker/
│   │   │   ├── AWOLApplication.java    # Application entrypoint & theme setup
│   │   │   ├── activity/
│   │   │   │   ├── SplashActivity.java        # Session-aware splash screen
│   │   │   │   ├── LoginActivity.java         # Role selector & demo login
│   │   │   │   ├── EmployeeMainActivity.java  # Employee attendance dashboard
│   │   │   │   ├── AdminMainActivity.java     # Administrator staff manager
│   │   │   │   └── SettingsActivity.java      # Geofence & theme preferences
│   │   │   ├── adapter/
│   │   │   │   ├── AttendanceAdapter.java     # Attendance log RecyclerView adapter
│   │   │   │   └── EmployeeAdapter.java       # Staff directory RecyclerView adapter
│   │   │   ├── database/
│   │   │   │   └── AttendanceDatabaseHelper.java # Local SQLite storage
│   │   │   ├── model/
│   │   │   │   ├── AttendanceRecord.java      # Attendance entity
│   │   │   │   └── Employee.java              # Employee entity
│   │   │   ├── receiver/
│   │   │   │   └── BootReceiver.java          # Boot completed auto-start receiver
│   │   │   ├── service/
│   │   │   │   └── LocationTrackingService.java # GPS Geofence foreground service
│   │   │   └── util/
│   │   │       ├── GeofenceHelper.java        # Distance & proximity math
│   │   │       ├── SessionManager.java        # SharedPreferences session manager
│   │   │       └── ThemeHelper.java           # Day/Night theme controller
│   │   └── res/                               # Layouts, vector drawables, themes, mipmaps
│   └── build.gradle                           # App-level dependencies & build settings
├── legacy_references/                         # Archived historical reference codebases
│   ├── AWOL/                                  # Original ITER university student portal
│   ├── android-employee-tracker/              # Original standalone GPS tracking service
│   ├── EASAndroid/                            # Original Employee Attendance System app
│   ├── Attendance_Register/                   # Original classroom roll-call app
│   ├── EmployeeAttendance/                    # Original PHP/MySQL attendance backend
│   ├── Attendance-taking-in-android-app/      # Original MGNREGA scheme stub
│   └── README.md                              # Detailed audit of reference projects
├── build.gradle                               # Root project build configuration
├── settings.gradle                            # Module inclusion (:app)
├── gradle.properties                          # AndroidX & JVM configuration
└── gradlew / gradlew.bat                      # Gradle wrapper executables
```

---

## Quick Demo Credentials

For testing and demonstration, pre-configured accounts are accessible via quick-login buttons on the sign-in screen:

| Role | Email | Password | Pre-seeded Records |
| :--- | :--- | :--- | :--- |
| **Employee** | `aaqib@company.com` | `1234` | Aaqib Khan (Engineering) |
| **Administrator** | `admin@company.com` | `1234` | Admin Director (Executive) |

---

## Building and Running

1. Open **Android Studio** (Electric Eel or newer recommended).
2. Select **Open** and choose the root `AWOL-Employee-tracker` directory.
3. Allow Gradle to sync the project.
4. Run the app on an Android Emulator or physical device (API Level 21+).
