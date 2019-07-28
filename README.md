# Malime

[![Build Status](https://app.bitrise.io/app/b9a83a0271f20f47/status.svg?token=5iXkOtZfs29XH4IaFFwYSQ&branch=v2)](https://app.bitrise.io/app/b9a83a0271f20f47)
[![codebeat badge](https://codebeat.co/badges/165302be-dea8-41c8-9d6e-1830f59183bb)](https://codebeat.co/projects/github-com-chesire-malime-master)
[![codecov](https://codecov.io/gh/Chesire/Malime/branch/v2/graph/badge.svg)](https://codecov.io/gh/Chesire/Malime)

Malime is an open source Android application to track your Anime & Manga progress, through the use of Kitsu.io.
[<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png"
      alt="Download from Google Play"
      height="80">](https://play.google.com/store/apps/details?id=com.chesire.malime)

[//]: # (Add pictures of the app 1 - 3)

## Features
Features include:

-    Track Anime and Manga series progress
-    Change your series state
-    Add new series to your lists
-    Quick access to your lists with minimal navigation
-    [Kitsu](https://kitsu.io)

## Development setup
Clone the project down and run it from Android studio, all dependencies will be handled by gradle.  
If there is an issue with the google-services.json missing, then a dummy one will need to be provided in the directory for whichever flavour is being built `app/src/debug` or `app/src/release`.

### Lint
All code should be run through [ktlint](https://ktlint.github.io/) to keep a consistent style throughout the code base. All issues must be fixed.  
To run ktlint:   
```sh
./gradlew ktlint
```

### Static Analysis
All code should be run through [detekt](https://github.com/arturbosch/detekt) to minimize any style issues or potential bugs.  
To run detekt:  
```sh
./gradlew detekt
```  
Any issues that are purposely not fixed in detekt should be suppressed using the `@Suppress("")` annotation.  
If any items raised by detekt conflict with ktlint, the ktlint rules should be followed.  

### Leaks
Running the application in debug will add a second application on the Android device labeled [LeakCanary](https://github.com/square/leakcanary), this is to detect any leaks that occur in the application.  
In order to get leak data LeakCanary should be provided with storage access.

## Tests
All viewmodels should be tested as throughly as possible.  
All other areas should be tested as much as possible, but currently are lacking tests...

To run tests:  
```sh
./gradlew test
```  
To run the UI tests:
```sh
./gradlew connectedDebugAndroidTest
```

## Why
There are already some really great Android tracking apps for Anime tracking portals, so why make another one?  
Originally this was a testing area to mess around with a bunch of Kotlin tools and mess around with the MyAnimeList API, but after a while it made sense to just make it into a proper application.

## Note
All work is now done on the V2 branch.
