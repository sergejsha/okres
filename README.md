[![Maven Central](http://img.shields.io/maven-central/v/de.halfbit/okres.svg)](https://central.sonatype.com/artifact/de.halfbit/okres)
[![maintenance-status](https://img.shields.io/badge/maintenance-experimental-blue.svg)](https://gist.github.com/taiki-e/ad73eaea17e2e0372efb76ef6b38f17b)

# 👌 OkRes

Simple multiplatform result monad library for Kotlin.

[The Result Monad](https://adambennett.dev/2020/05/the-result-monad/)

![Architecture diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/sergejsha/okres/master/documentation/architecture.v1.iuml)

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
