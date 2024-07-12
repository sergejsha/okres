package de.halfbit.okres

public sealed class Res<out Ok, out Err>
public data class OkRes<out Ok>(val ok: Ok) : Res<Ok, Nothing>()
public data class ErrRes<out Err>(val err: Err) : Res<Nothing, Err>()

public val <Ok> Ok.ok: OkRes<Ok> get() = OkRes(this)
public val <Err> Err.err: ErrRes<Err> get() = ErrRes(this)

public fun <Ok> ok(value: Ok): OkRes<Ok> = OkRes(value)
public fun <Err> err(value: Err): ErrRes<Err> = ErrRes(value)

/** Object representing a generic success result. Use it instead of Unit. */
public data object Success

/** Object representing a generic error result. Use it instead of Unit. */
public data object Error

public inline fun <Ok, Err> Res<Ok, Err>.onOk(
    block: (ok: OkRes<Ok>) -> Unit
): Res<Ok, Err> {
    if (this is OkRes) {
        block(this)
    }
    return this
}

public inline fun <Ok, Err> Res<Ok, Err>.onErr(
    block: (err: ErrRes<Err>) -> Unit
): Res<Ok, Err> {
    if (this is ErrRes) {
        block(this)
    }
    return this
}

public inline fun <Ok, Ok2, Err> Res<Ok, Err>.andThen(
    block: (Ok) -> Res<Ok2, Err>
): Res<Ok2, Err> {
    return when (val actual = this) {
        is OkRes -> block(actual.ok)
        is ErrRes -> ErrRes(actual.err)
    }
}
