package okres

/**
 * Represents a result of an execution of a function. The function
 * returning [Res] must not throw any exception. [Res.Er] must be
 * returned instead.
 *
 * Here is the example:
 * ```
 * interface FetchEmails {
 *   operator fun invoke(): Res<Value, Failure>
 *
 *   sealed interface Value {
 *     object NoEmails : Value
 *     data class Emails(val emails: List<Email>) : Value
 *   }
 *
 *   sealed interface Failure {
 *      object BadCredentials : Failure
 *      object BadConnection : Failure
 *   }
 * }
 *
 * fun main(fetchEmails: FetchEmails) {
 *   fetchEmails()
 *     .onOk { value ->
 *       when(value) {
 *         Value.NoEmails -> println("no emails")
 *         Value.Emails -> println(value.emails)
 *       }
 *     }
 *     .onEr { failure ->
 *       when(failure) {
 *         Failure.BadCredentials -> println("bad credentials")
 *         Failure.BadConnection -> println("bad connection")
 *       }
 *     }
 * }
 * ```
 */
sealed interface Res<out Ok, out Er> {
    data class Ok<out Ok>(val value: Ok) : Res<Ok, Nothing>
    data class Er<out Er>(val value: Er) : Res<Nothing, Er>
}

val <Ok> Ok.ok get() = Res.Ok(this)
val <Er> Er.er get() = Res.Er(this)

inline fun <Ok, Er> Res<Ok, Er>.onOk(
    action: (ok: Ok) -> Unit
): Res<Ok, Er> = apply {
    if (this is Res.Ok) action(value)
}

inline fun <Ok, Er> Res<Ok, Er>.onEr(
    action: (er: Er) -> Unit
): Res<Ok, Er> = apply {
    if (this is Res.Er) action(value)
}
