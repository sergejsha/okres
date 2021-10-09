package okres

/**
 * Represents a result of a computation.
 *
 * - if the computation succeeds it shall return `Res.Ok`.
 * - if the computation fails, it shall return `Res.Err`.
 *
 * The computation must never throw any exceptions. `Res.Err`
 * must be returned instead.
 */
sealed interface Res<out Ok, out Err> {
    data class Ok<out Ok>(val value: Ok) : Res<Ok, Nothing>
    data class Err<out Err>(val error: Err) : Res<Nothing, Err>
}

/** Returns [Res.Ok] with the receiver value. */
val <Ok> Ok.ok get() = Res.Ok(this)

/** Returns [Res.Err] with the receiver value. */
val <Err> Err.err get() = Res.Err(this)

/** Executes given `action` if the receiver is [Res.Ok]. */
inline fun <Ok, Er> Res<Ok, Er>.onOk(action: (ok: Ok) -> Unit): Res<Ok, Er> =
    apply { if (this is Res.Ok) action(value) }

/** Executes given `action` if the receiver is [Res.Err]. */
inline fun <Ok, Err> Res<Ok, Err>.onErr(action: (err: Err) -> Unit): Res<Ok, Err> =
    apply { if (this is Res.Err) action(error) }

/**
 * Transforms the value of `Res.Ok` to another value by applying `transform` function.
 *
 * - if the result is `Res.Ok`, the `transform` block will be called.
 * - if the result is `Res.Err`, the same error will propagate through.
 */
inline fun <Ok, Err> Res<Ok, Err>.map(
    transform: (value: Ok) -> Ok
): Res<Ok, Err> = when (this) {
    is Res.Ok -> Res.Ok(transform(value))
    is Res.Err -> this
}

/**
 * Transforms the value of `Res.Err` to another error by applying `transform` function.
 *
 * - if the result is `Res.Err`, the `transform` block will be called.
 * - if the result is `Res.Ok`, the same value will propagate through.
 */
inline fun <Ok, Err, Err2> Res<Ok, Err>.mapErr(
    block: (err: Err) -> Err2
): Res<Ok, Err2> = when (this) {
    is Res.Ok -> this
    is Res.Err -> Res.Err(block(error))
}

/**
 * Chains together a sequence of computations that can fail. Next block
 * will only be called if the previous computation was [Res.Ok].
 */
inline fun <Ok, Err, Ok2> Res<Ok, Err>.andThen(
    nextBlock: (value: Ok) -> Res<Ok2, Err>
): Res<Ok2, Err> = when (this) {
    is Res.Ok -> nextBlock(value)
    is Res.Err -> this
}
