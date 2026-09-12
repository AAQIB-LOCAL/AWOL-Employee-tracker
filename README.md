# AWOL - Employee Tracker

AWOL Employee Tracker is an Android attendance management application focused on employee clock-in/out, biometric verification, workplace proximity, local attendance storage, and separate employee and administrator experiences.

## Current capabilities

- Employee clock-in and clock-out flows with biometric verification.
- Live shift timing and attendance status.
- Employee attendance history and summary views.
- Administrator workforce and employee management views.
- GPS-based workplace proximity checks and configurable geofence radius.
- Foreground location tracking with boot-time service handling.
- Offline-first local SQLite storage for employee and attendance data.
- Session management and runtime light/dark theme switching.

## Current project structure

```text
AWOL-Employee-tracker/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/awol/employeetracker/
│   │   │   ├── AWOLApplication.java
│   │   │   ├── activity/
│   │   │   ├── adapter/
│   │   │   ├── database/
│   │   │   ├── model/
│   │   │   ├── receiver/
│   │   │   ├── service/
│   │   │   └── util/
│   │   └── res/
│   └── build.gradle
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── .gitignore
```

The repository contains only the active application and project files. Historical reference projects and generated repository bloat were removed before the production-readiness work began.

## Production-readiness roadmap

The application is being hardened incrementally rather than through a single large rewrite.

### Phase 0 — Production baseline

- Establish the current application boundary.
- Keep repository documentation aligned with the actual source tree.
- Define explicit production-readiness gates for subsequent phases.
- Preserve the existing application behavior while the baseline is established.

### Phase 1 — Build and Android platform modernization

- Establish a supported Android SDK and Gradle toolchain.
- Modernize AndroidX and dependency versions deliberately.
- Remove obsolete build configuration and unused dependencies.
- Validate debug and release builds after each modernization step.

### Phase 2 — Security and authentication hardening

- Replace demo-oriented authentication with a production authentication boundary.
- Review authorization for employee and administrator capabilities.
- Remove hardcoded credentials and other sensitive values.
- Harden local session and biometric handling.

### Phase 3 — Data and synchronization architecture

- Review the SQLite schema and data lifecycle.
- Define reliable synchronization and conflict handling.
- Establish a production backend boundary where required.
- Protect local and remote attendance data.

### Phase 4 — Attendance and location reliability

- Harden geofencing and foreground-service behavior across supported Android versions.
- Handle permissions, battery restrictions, connectivity loss, and service restarts predictably.
- Validate clock-in/out and attendance persistence under failure conditions.

### Phase 5 — UX and operational polish

- Improve accessibility, validation, empty states, loading states, and recoverable errors.
- Standardize employee and administrator flows.
- Remove demo-only presentation and behavior from production paths.

### Phase 6 — Testing and release readiness

- Establish meaningful unit, integration, and UI coverage around critical attendance flows.
- Add release validation and manual deployment checklists.
- Verify logging, diagnostics, privacy expectations, and rollback readiness.
- Produce a repeatable production release process without introducing GitHub Actions.

## Production gates

A phase is considered production-ready only when the relevant area has:

- A defined owner and clear behavior boundary.
- No known blocker-level security or data-integrity issue within its scope.
- Validation for the primary success path and important failure paths.
- No unnecessary dependency or configuration introduced solely for convenience.
- Documentation that matches the implemented behavior.
- A manual verification path suitable for release testing.

## Build and deployment

1. Open the repository in Android Studio.
2. Allow Gradle to sync using the project toolchain supported by the current phase.
3. Run the application on a supported emulator or physical device.
4. Execute the relevant manual validation checks for the phase being released.
5. Build and deploy the release artifact manually.

GitHub Actions are intentionally not part of this repository's deployment workflow.

## Demo accounts

The current application contains demo-oriented login flows for local testing. These are development conveniences and are not considered production authentication. Authentication hardening is explicitly tracked in Phase 2.

## Production target

The target is a maintainable, secure, reliable Android attendance product with clear employee/admin boundaries, dependable attendance records, resilient location behavior, controlled data synchronization, and a repeatable manual release process.
