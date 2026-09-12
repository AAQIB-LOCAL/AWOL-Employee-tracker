# Legacy References Archive

This directory archives the original reference and experimental repositories that were previously scattered across the root directory of `AWOL-Employee-tracker`. 

All active, stabilized, and unified application code is now centralized in the root [`app/`](../app/) module.

---

## Directory Index & Historical Audit

### 1. `AWOL/`
- **Original Source**: `codex.codex_iter.www.awol`
- **Domain**: University student attendance and grade portal client (ITER / SOA University).
- **Core Elements**: AndroidX, Material Design theming (Dark/Light mode via `ThemeHelper`), splash screen, bottom sheet login, and modern ViewBinding architecture.
- **Academic Features Beyond Employee Theme**: Semester exam results (`ResultActivity`, `DetailedResultActivity`, SGPA/CGPA), subject timetables, in-app self-updater using `PRDownloader`, Firestore maintenance mode kill-switch, OneSignal push notifications, and multi-account student switcher.

### 2. `android-employee-tracker/`
- **Original Source**: `com.codilar.empattendencetrack`
- **Domain**: Automated GPS geofence location tracker for field employees.
- **Core Elements**: Background `LocationService` using Fused Location / ReactiveLocationProvider, calculating distance against target coordinates, and automatic IN/OUT logging.
- **Features Beyond Employee Theme**: Direct SD card logging to `/sdcard/Location_log.txt` with in-app log viewer.

### 3. `EASAndroid/`
- **Original Source**: `com.example.easandroid`
- **Domain**: Enterprise Employee Attendance System.
- **Core Elements**: Dual-role architecture (Admin and Staff/Employee), Mark In / Mark Out check-in workflows, offline SQLite caching with sync flags (`IS_SYNC_IN`, `IS_SYNC_OUT`), and REST API synchronization via Volley.
- **Features**: Admin employee registration with Firebase Storage photo uploads, live present employees list, attendance reports, and biometric fingerprint simulation (`USE_FINGERPRINT`).

### 4. `Attendance_Register/`
- **Original Source**: `com.attendance.myproject.attendanceregister` (IPEC College)
- **Domain**: Academic classroom roll-call register.
- **Features**: Teacher roll-call portal with subject picker, student portal for subject attendance percentages, and Firebase Realtime Database.

### 5. `EmployeeAttendance/`
- **Original Source**: `com.example.employeeattendance`
- **Domain**: Legacy PHP MySQL attendance app.
- **Features**: PHP backend scripts (`login.php`, `register.php`, `dbconnect.php`) with dynamic table creation, and period-based analytics tabs (Daily, Weekly, Monthly).

### 6. `Attendance-taking-in-android-app/`
- **Original Source**: Conceptual repository stub.
- **Domain**: MGNREGA rural worker attendance with automated payroll calculation.
