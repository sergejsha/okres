# Okres
Concise result type representing success and error values

# Example
```kotlin
interface FetchEmails {
  operator fun invoke(): Res<Value, Failure>
  
  sealed interface Value {
    object NoEmails : Value
    data class Emails(val emails: List<Email>) : Value
  }

  enum class Failure {
     BadCredentials,
     BadConnection
  }
}

fun main(fetchEmails: FetchEmails) {
  fetchEmails()
    .onOk { value ->
      when(value) {
        Value.NoEmails -> println("no emails")
        Value.Emails -> println(value.emails)
      }
    }
    .onEr { failure ->
      when(failure) {
        Failure.BadCredentials -> println("bad credentials")
        Failure.BadConnection -> println("bad connection")
      }
    }
}
```

# Binaries
```
// for java projects
dependencies {
    implementation 'de.halfbit:okres-jvm:1.0'
}
```
