# Release Readiness Notes

This project is in a good state for development sharing, but before publishing it more widely, these checks are worth doing.

## Product and UI

- Review all screens for hardcoded text and move reusable text into `strings.xml`
- Check button sizes and spacing on small devices
- Make sure the category flow is still intuitive from login to posting

## Firebase and data

- Confirm `google-services.json` is added locally before running release builds
- Verify Firebase Authentication Email/Password is enabled
- Verify Firestore security rules are not left wide open for production
- Confirm the `posts` collection and `replies` subcollections match the current app logic

## Functional checks

- Login with an existing account
- Create a new account
- Create posts in more than one category
- Open the feed and verify category filtering
- Like, reply to, and delete posts
- Test behavior with empty feeds and empty replies

## Code cleanup still worth doing

- Rename `PostAdpter.kt` to `PostAdapter.kt`
- Replace remaining hardcoded layout text with string resources
- Review whether delete should be limited to the post owner only
- Consider adding simple loading and empty states across more screens

## Repo hygiene

- Keep `google-services.json` out of the public repo
- Keep `local.properties`, `.idea`, and build artifacts ignored
- Update the README whenever the app flow changes
