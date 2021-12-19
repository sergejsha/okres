# Okres
Concise result type representing success and error values. 
Reduces the pain of dealing with exceptions in Kotlin.

```kotlin
sealed interface Res<out Value, out Error> {
    data class Ok<out Value>(val value: Value) : Res<Value, Nothing>
    data class Err<out Error>(val error: Error) : Res<Nothing, Error>
}
```

# Usage example
```kotlin
fun main(
    fetchEmails: FetchEmails,
    processEmails: ProcessEmails,
) {
  fetchEmails()
    .andThen { emails -> processEmails(emails) }
    .onOk { value -> 
        println("emails were fetched and processed") 
    }
    .onErr { error -> 
        when(error) {
            FetchEmails.Error.BadCredentials -> handleBadCredentials()
            FetchEmails.Error.BadConnection -> handleBadConnected()
            ProcessEvents.Error -> handleProcessingError()
        } 
    }
}

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

interface ProcessEvents {
    operator fun invoke(emails: List<Email>): Res<Error, Unit>

    object Error
}
```

# Binaries
```gradle
// for jvm projects
dependencies {
    implementation 'de.halfbit:okres-jvm:2.1'
}
```
