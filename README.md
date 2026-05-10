# CrickU

CrickU is a small Android app I built around a simple idea: sports fans should have one place where they can log in, pick a category they care about, post their thoughts, and react to what other people are saying. Right now the app is focused on a few core sports categories like Cricket, Football, and Hockey, and the flow is intentionally beginner-friendly so the project stays easy to understand and improve.

The app uses Firebase Authentication for login and signup, and Firebase Firestore for storing posts and replies. A user signs in, reaches the home screen, chooses a category, chooses whether they want to post stats or a prediction, and then joins the conversation. Posts are shown in a scrollable feed, and users can like, reply to, and delete posts.

## What the app currently does

- Login and signup with Firebase Authentication
- Create sports posts
- Browse posts in a feed
- Filter the feed by category
- Like posts
- Reply to posts
- Delete posts

## Main project flow

The current user journey looks like this:

`LoginActivity -> MainActivity -> CategoryActivity -> OptionActivity -> PostActivity / FeedActivity`

In simple terms:

- `LoginActivity` handles email/password authentication
- `MainActivity` works as the landing screen after login
- `CategoryActivity` lets the user choose a sports category
- `OptionActivity` lets the user choose the type of post
- `PostActivity` handles creating posts and loading related posts
- `FeedActivity` handles the broader category-based feed
- `PostAdapter` handles the UI and actions for each post item

## Tech stack

- Kotlin
- Android Studio
- RecyclerView
- Firebase Authentication
- Firebase Firestore

## Project structure

Important app files live under:

- `app/src/main/java/com/example/cricku/`
- `app/src/main/res/layout/`
- `app/src/main/res/values/`
- `app/src/main/AndroidManifest.xml`

Some key files:

- `LoginActivity.kt`
- `MainActivity.kt`
- `CategoryActivity.kt`
- `OptionActivity.kt`
- `PostActivity.kt`
- `FeedActivity.kt`
- `PostAdpter.kt`
- `ReplyAdapter.kt`
- `Post.kt`
- `Reply.kt`

## Firebase note

This public repository does **not** include `app/google-services.json`.

That file was intentionally left out so Firebase project details do not get pushed publicly. If you clone this repo and want to run the app, add your own Firebase config file in:

`app/google-services.json`

There is a short setup note in [FIREBASE_SETUP.md](FIREBASE_SETUP.md).

## Current known cleanup points

This project is working, but it is still in active development. A few things are worth polishing next:

- Move more hardcoded UI text into `strings.xml`
- Tighten post ownership rules before allowing deletes in a production build
- Improve reply rendering from plain text into a stronger threaded UI
- Add a cleaner release flow with basic testing and Firebase setup verification
- Rename `PostAdpter.kt` to `PostAdapter.kt` at some point for consistency

## Running the project

1. Clone the repository
2. Open it in Android Studio
3. Add your own `google-services.json` inside the `app` folder
4. Make sure Firebase Email/Password sign-in is enabled
5. Sync Gradle
6. Run the app on an emulator or device

## Why this repo looks the way it does

This project has been built iteratively while features were being tested and improved one by one. The focus so far has been to keep the app understandable, working, and easy to build on instead of trying to make it overly complex too early.

If you are reading this as a developer, the best way to approach CrickU is to treat it like a solid early-stage Android/Firebase project: simple structure, real features, and a lot of room to refine the product experience.
