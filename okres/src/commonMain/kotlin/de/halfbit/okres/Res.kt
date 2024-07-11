package de.halfbit.okres

public sealed class Res<out Ok, out Err>
public data class OkRes<out Ok, out Err>(val ok: Ok) : Res<Ok, Err>()
public data class ErrRes<out Ok, out Err>(val err: Err) : Res<Ok, Err>()

public inline fun <Ok, Err> Res<Ok, Err>.onOk(
    block: (ok: OkRes<Ok, Err>) -> Unit
): Res<Ok, Err> {
    if (this is OkRes) {
        block(this)
    }
    return this
}

public inline fun <Ok, Err> Res<Ok, Err>.onErr(
    block: (err: ErrRes<Ok, Err>) -> Unit
): Res<Ok, Err> {
    if (this is ErrRes) {
        block(this)
    }
    return this
}

public inline fun <Ok, Ok2, Err> Res<Ok, Err>.andThen(
    block: () -> Res<Ok2, Err>
): Res<Ok2, Err> {
    return when (val actual = this) {
        is OkRes -> block()
        is ErrRes -> ErrRes(actual.err)
    }
}
