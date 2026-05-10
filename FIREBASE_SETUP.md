# Firebase Setup

This repository does not include `app/google-services.json`.

That is intentional. The project was pushed publicly, so the Firebase config file was left out to avoid exposing project-specific configuration in the repo.

## To connect Firebase again

1. Open your Firebase project in the Firebase Console
2. Make sure the Android app package name matches:

   `com.example.cricku`

3. Download the Firebase Android config file
4. Place it here:

   `app/google-services.json`

## Firebase services currently expected by the app

- Firebase Authentication
- Cloud Firestore

## Auth setup

Inside Firebase Authentication:

- enable `Email/Password` sign-in

## Firestore setup

Create or use a Firestore database and make sure the app can read from a collection named:

`posts`

Replies are expected under:

`posts/{postId}/replies`

## Helpful reminder

If category-based feed queries are used with ordering, Firestore may ask for a composite index. If that happens, Firebase usually gives you a direct link to create the required index from the error message.
