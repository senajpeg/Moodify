 An Android application that offers AI-powered music and playlist recommendations based on the user's 
mood.

 Technologies:
 
 AI Integration: Generates playlist recommendations based on the user’s mood.
 
 Deezer API (via Retrofit): Fetches mood-based songs and playlists for free without requiring user login.
 
 Firebase Auth (Google Sign-In, Email/Password, Password Reset, Email Verification)
 Firebase Firestore, Firebase Storage (profile pictures)
 WorkManager: Sends reminder notifications (“Your evening music suggestion is ready”).
 MVVM (Model-View-ViewModel): Application architecture.
 Coroutines + Flow: Manage asynchronous operations and data streams.
 Hilt (Dagger-Hilt): Dependency injection in Moodify.
 Image Picker (Jetpack Activity Result API, Photo Picker): Allows users to select photos from their gallery.
 Used ViewModel structure to separate data management from the UI; combined data from Retrofit (Deezer 
API) and Firebase into a single flow and delivered it to the UI via StateFlow.
