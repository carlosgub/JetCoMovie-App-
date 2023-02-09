# Jetpack Compose Movie App (Compose + Flow + Hilt + Room + MVVM + Clean + Retrofit)

<img width="1141" alt="image" src="https://user-images.githubusercontent.com/30916886/215865185-2c72f73b-7eda-43a5-955f-103d195cecc5.png">

This is an example of an Android App who uses Jetpack Compose to draw the UI, Kotlin Coroutines Flow to retrieve a list of the recent movies from https://www.themoviedb.org.

## How use this app
- Request an api key from themoviedb https://developers.themoviedb.org/3/getting-started/authentication and later put that key in the build.gradle app file.
<img width="474" alt="image" src="https://user-images.githubusercontent.com/30916886/215863998-a1dc026c-3b3c-45c6-a8fc-478302d85b6a.png">

## V2 ![](https://geps.dev/progress/100)
- [X] Add the Movie Detail View.
- [X] Add logic to the booking buttom in the Movie Detail View save the movie in the database to later use this database for the Tickets View.

## V3 ![](https://geps.dev/progress/100)
- [X] Add the Search View.
- [X] Add the Tickets View.

## V4 ![](https://geps.dev/progress/100)
- [X] Create text styles and dimens for the views and do some refactor.
- [X] Add light/dark theme (change the theme in your device settings)

## V5 ![](https://geps.dev/progress/70)
- [X] Add klint and detekt to the project
- [X] Add Github Action to the repository to run klint and detekt
- [ ] Add Unit test.

## V6
- [ ] Add Jacoco to review code test coverage
- [ ] Add support to different Screens.

## V7
- [ ] Add a "Login" in the Profile View.
- [ ] Add pagination to the Search Screen.

## Features

### Movie Screen

https://user-images.githubusercontent.com/30916886/216682426-ef729ded-f201-4635-aaae-129d7581ebb8.mp4

### Search Screen

https://user-images.githubusercontent.com/30916886/216682509-69eb9ac3-8312-4707-be5a-739dde1c78fa.mp4

### Ticket Screen

https://user-images.githubusercontent.com/30916886/216682539-eb0ab059-642d-41c4-9a51-b6f9f0bf6cc2.mp4

### Movie Detail Screen

https://user-images.githubusercontent.com/30916886/216682562-b516596d-403c-4643-8b36-fb81186795e9.mp4

### Light/Dark Theme

https://user-images.githubusercontent.com/30916886/217094735-80182247-a023-4062-9527-c8de205e3812.mp4

## Tech Stack

- [Kotlin](https://kotlinlang.org/) - Official programming language for Android development.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Android’s modern toolkit for building native UI.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) and [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html#asynchronous-flow) - Official Kotlin's tooling for performing asynchronous work.
- [Android Jetpack](https://developer.android.com/jetpack) - Jetpack is a suite of libraries to help developers build state-of-the-art applications.
	- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - The ViewModel is designed to store and manage UI-related data in a lifecycle conscious way.
	- [Room](https://developer.android.com/topic/libraries/architecture/room) - The Room library provides an abstraction layer over SQLite to allow for more robust database access.
	- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Hilt is a dependency injection library for Android.
- [Accompanist](https://github.com/google/accompanist) - A collection of extension libraries for Jetpack Compose.
- [Retrofit](https://github.com/square/retrofit) - A library for building REST API clients.
- [Coil](https://github.com/coil-kt/coil) - An image loading library.
- [Detekt](https://github.com/detekt/detekt) - A static code analysis library for Kotlin.
- [Ktlint](https://github.com/pinterest/ktlint) [(Kotlinter)](https://github.com/jeremymailen/kotlinter-gradle) - A library for formatting Kotlin code according to official guidelines.
- [Twitter Jetpack Compose Rules](https://twitter.github.io/compose-rules/)
- [Gradle's Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) - Gradle’s Kotlin DSL is an alternative syntax to the Groovy DSL with an enhanced editing experience.

# References

- [Jetpack Compose](https://developer.android.com/jetpack/compose)

- [Design](https://dribbble.com/shots/18839708-Movie-Tickets-Mobile-App)

- [Build custom bottom navigation Bar](https://www.boltuix.com/2022/08/custom-bottom-navigation-bar.html)

- [Background Gradient](https://semicolonspace.com/android-jetpack-compose-gradient/)

- [Library where I got the horizontal viewpager](https://github.com/google/accompanist)

- [CompositionLocal for the light/dark teme](https://developer.android.com/jetpack/compose/compositionlocal)

- [Add more colors to the material theme](https://gustav-karlsson.medium.com/extending-the-jetpack-compose-material-theme-with-more-colors-e1b849390d50)

## Contributors ✨

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/carlosgub"><img src="https://avatars1.githubusercontent.com/u/30916886?s=460&v=4" width="100px;" alt=""/><br /><sub><b>Carlos Ugaz</b></sub></a><br /></td>
  </tr>
</table>
