[![Maven Central](http://img.shields.io/maven-central/v/de.halfbit/okres.svg)](https://central.sonatype.com/artifact/de.halfbit/logger)
![maintenance-status](https://img.shields.io/badge/maintenance-experimental-blue.svg)

# 👌 OkRes

Simple multiplatform result monad library for Kotlin.

[The Result Monad](https://adambennett.dev/2020/05/the-result-monad/)

# Dependencies

In `gradle/libs.versions.toml`

```toml
[versions]
kotlin = "2.0.0"
okres = "5.0"

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

# Release

1. Bump version in `root.publication.gradle.kts` of the root project
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
