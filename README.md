# FamilySafe Android App

Privacy-first family location app starter.

## Build APK without Android Studio

This project includes GitHub Actions, so you can build an APK online without installing Android Studio.

### 1. Create a GitHub repository

On GitHub, create a new repository, for example:

`FamilySafe`

Keep it private if this is for your family.

### 2. Upload the project

Extract this ZIP on your computer and upload **all files and folders inside the project folder** to the GitHub repository.

Important files include:

- `app/`
- `build.gradle.kts`
- `settings.gradle.kts`
- `gradlew`
- `gradle/`
- `.github/workflows/build-apk.yml`

### 3. Run the build

In your GitHub repository:

**Actions → Build FamilySafe APK → Run workflow**

The workflow will:

1. Set up Java 17
2. Set up Android SDK
3. Install Android API 35/build tools
4. Build the debug APK
5. Upload the APK as a downloadable workflow artifact

### 4. Download the APK

After the workflow finishes:

**Actions → Build FamilySafe APK → completed run → Artifacts → FamilySafe-debug-apk**

Download the artifact ZIP and extract it. Inside will be:

`app-debug.apk`

Copy that APK to your Android phone and install it.

## Important

This APK is the current FamilySafe starter/prototype. It is **not yet a real family location-sharing service**.

The current app includes the privacy-first UI and location permission flow, but production functionality still needs:

- User authentication
- Family invitations
- Consent-based family membership
- Real-time location database
- Secure Firebase/Firestore rules
- Background/foreground location service
- Map display
- SOS notifications
- Optional location history
- Abuse prevention and audit logging

The app must never secretly track another person's phone. Every person should explicitly consent to location sharing.

## GitHub Actions

The build workflow is:

`.github/workflows/build-apk.yml`

It builds a debug APK automatically whenever you push to `main`, and it can also be started manually from the Actions tab.
