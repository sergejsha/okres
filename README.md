[![Maven Central](http://img.shields.io/maven-central/v/de.halfbit/okres.svg)](https://central.sonatype.com/artifact/de.halfbit/okres)
[![maintenance-status](https://img.shields.io/badge/maintenance-experimental-blue.svg)](https://gist.github.com/taiki-e/ad73eaea17e2e0372efb76ef6b38f17b)

# 👌 OkRes

Simple multiplatform result monad library for Kotlin.

[The Result Monad](https://adambennett.dev/2020/05/the-result-monad/)

![Architecture diagram](https://www.plantuml.com/plantuml/svg/NP5HJiCm38RVSmghJo2D7g2gq00XtgYqxG9PPjRIDAbY5wXWTyTPqz7HbpY_l_pzdQo9TU2XiuhQnlKww0vwRLfzn2sF5Y6WOUsE5bNT712mwtc43hiz1feaOWpeUUmJOffW15V6QYB8DaYb7nYgTWLnVGxXFWDD80tuLY2VTrLxSvjAwLzgGLATv-Ubq_q5bNfYq-QQrEwmgz69U9gJL6tC4dEyUguRTvnodEMP4SS89M3rbtPgA1LJbpzvdevTjga_-pouiSGlHaUDTpHPY9O6rbIrnUIGzdAyI1xBAHgAunRmwBr5xP8ct58yAPMU3B8NT1WQ0m5fi4pPVCFxTpGCsy4O95fAukN3bSNEztSMFz32s-0CI_x_DbaOt9Dc4w_e_4G5o59kLh2hW__KUulJn0ik_cADxZ1qzXS0)

# Usage example

```kotlin
// given message processing functions
fun receiveMessage(): Res<String, Error> =
    "Message in a bottle".asOk

fun decryptMessage(message: String): Res<String, Error> =
    if (message.isNotBlank()) message.asOk else Error.asErr

fun readMessage(message: String): Res<String, Error> =
    runCatchingRes { /* ... */ message }.mapErr { Error }

fun forwardMessage(message: String): Res<String, Error> =
    runCatchingRes { /* ... */ message }.mapErr { Error }

// process the message and log results
receiveMessage()
    .andThen(::decryptMessage)
    .andThen(::readMessage)
    .andThen(::forwardMessage)
    .onOk { message -> println("Message processed: $message") }
    .onErr { error -> println("Processing failed: $error") }
```

# Dependencies

In `gradle/libs.versions.toml`

```toml
[versions]
kotlin = "2.0.0"
okres = "5.2"

[libraries]
okres = { module = "de.halfbit:okres", version.ref = "okres" }

[plugins]
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
```

In `shared/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.okres)
        }
    }
}
```

# Releasing

1. Bump version in `root.publication.gradle.kts`
2. `./gradlew clean build publishAllPublicationsToCentralRepository`

# License

```
Copyright 2023-2024 Sergej Shafarenka, www.halfbit.de

You are FREE to use, copy, redistribute, remix, transform, and build 
upon the material or its derivative FOR ANY LEGAL PURPOSES.

Any distributed derivative work containing this material or parts 
of it must have this copyright attribution notices.

The material is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
OR CONDITIONS OF ANY KIND, either express or implied.
```
