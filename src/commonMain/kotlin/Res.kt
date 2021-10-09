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
sealed interface Res<out Value, out Err> {
    data class Ok<out Value>(val value: Value) : Res<Value, Nothing>
    data class Err<out Error>(val error: Error) : Res<Nothing, Error>
}

/** Wraps receiver value with [Res.Ok]. */
val <Ok> Ok.ok get() = Res.Ok(this)

/** Wraps receiver value with [Res.Err]. */
val <Err> Err.err get() = Res.Err(this)

/** Executes given `action` if the receiver is [Res.Ok]. */
inline fun <Value, Error> Res<Value, Error>.onOk(
    action: (ok: Value) -> Unit
): Res<Value, Error> = apply {
    if (this is Res.Ok) action(value)
}

/** Executes given `action` if the receiver is [Res.Err]. */
inline fun <Value, Error> Res<Value, Error>.onErr(
    action: (err: Error) -> Unit
): Res<Value, Error> = apply {
    if (this is Res.Err) action(error)
}

/**
 * Transforms the value of `Res.Ok` to another value by applying `transform` function.
 *
 * - if the result is `Res.Ok`, the `transform` block will be called.
 * - if the result is `Res.Err`, the same error will propagate through.
 */
inline fun <Value, Error, Value2> Res<Value, Error>.map(
    transform: (value: Value) -> Value2
): Res<Value2, Error> = when (this) {
    is Res.Ok -> Res.Ok(transform(value))
    is Res.Err -> this
}

/**
 * Transforms the value of `Res.Err` to another error by applying `transform` function.
 *
 * - if the result is `Res.Err`, the `transform` block will be called.
 * - if the result is `Res.Ok`, the same value will propagate through.
 */
inline fun <Value, Error, Error2> Res<Value, Error>.mapErr(
    block: (err: Error) -> Error2
): Res<Value, Error2> = when (this) {
    is Res.Ok -> this
    is Res.Err -> Res.Err(block(error))
}

/**
 * Chains together a sequence of computations that can fail. Next block
 * will only be called if the previous computation was [Res.Ok].
 */
inline fun <Value, Error, Value2> Res<Value, Error>.andThen(
    nextBlock: (value: Value) -> Res<Value2, Error>
): Res<Value2, Error> = when (this) {
    is Res.Ok -> nextBlock(value)
    is Res.Err -> this
}
