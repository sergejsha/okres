# Okres
Concise result type representing success and error values. Reduces the pain of dealing with exceptions in Kotlin.

```kotlin
sealed interface Res<out Value, out Error> {
    data class Ok<out Value>(val value: Value) : Res<Value, Nothing>
    data class Err<out Error>(val error: Error) : Res<Nothing, Error>
}
```

# Usage example
```kotlin
interface FetchEmails {
  operator fun invoke(): Res<Value, Error>
  
  sealed interface Value {
    object NoEmails : Value
    data class Emails(val emails: List<Email>) : Value
  }

  enum class Error {
     BadCredentials,
     BadConnection
  }
}

fun main(fetchEmails: FetchEmails) {
  fetchEmails()
    .onOk { value ->
      // do something with the value
    }
    .onErr { error ->
      // handle error
    }
}
```

# Binaries
```gradle
// for jvm projects
dependencies {
    implementation 'de.halfbit:okres-jvm:2.1'
}
```
