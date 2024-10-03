package de.halfbit.okres

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public sealed class Res<out Ok, out Err>
public data class OkRes<out Ok>(val value: Ok) : Res<Ok, Nothing>()
public data class ErrRes<out Err>(val error: Err) : Res<Nothing, Err>()

public val <Ok> Ok.asOk: OkRes<Ok> get() = OkRes(this)
public val <Err> Err.asErr: ErrRes<Err> get() = ErrRes(this)

public fun <Ok> ok(value: Ok): OkRes<Ok> = OkRes(value)
public fun <Err> err(value: Err): ErrRes<Err> = ErrRes(value)

public val <Ok, Err> Res<Ok, Err>.isOk: Boolean get() = this is OkRes
public val <Ok, Err> Res<Ok, Err>.isErr: Boolean get() = this is ErrRes

/** Object representing a generic success result. Use it instead of Unit. */
public data object Success

/** Object representing a generic error result. Use it instead of Unit. */
public data object Error

public inline fun <Ok, Err> Res<Ok, Err>.onOk(
    block: (value: Ok) -> Unit
): Res<Ok, Err> {
    if (this is OkRes) {
        block(value)
    }
    return this
}

public inline fun <Ok, Err> Res<Ok, Err>.onErr(
    block: (error: Err) -> Unit
): Res<Ok, Err> {
    if (this is ErrRes) {
        block(error)
    }
    return this
}

public inline fun <Ok, Ok2, Err> Res<Ok, Err>.andThen(
    block: (value: Ok) -> Res<Ok2, Err>
): Res<Ok2, Err> {
    return when (val actual = this) {
        is OkRes -> block(actual.value)
        is ErrRes -> err(actual.error)
    }
}

public inline fun <Ok, Err> Res<Ok, Err>.onRes(
    onOk: (value: Ok) -> Unit,
    onErr: (error: Err) -> Unit
): Res<Ok, Err> {
    when (this) {
        is OkRes -> onOk(value)
        is ErrRes -> onErr(error)
    }
    return this
}

public inline fun <Ok, Err, R> Res<Ok, Err>.map(
    onOk: (value: Ok) -> R,
    onErr: (error: Err) -> R
): R {
    return when (this) {
        is OkRes -> onOk(value)
        is ErrRes -> onErr(error)
    }
}

/** Maps pure error to pure error type. */
public inline fun <Ok, Err, R> Res<Ok, Err>.mapErr(
    block: (error: Err) -> R
): Res<Ok, R> {
    return when (this) {
        is OkRes -> ok(this.value)
        is ErrRes -> err(block(error))
    }
}

/** Maps pure error to Res type. Use it when mapping function can turn an error also into ok type. */
public inline fun <Ok, Err, R> Res<Ok, Err>.mapErrRes(
    block: (error: Err) -> Res<Ok, R>
): Res<Ok, R> {
    return when (this) {
        is OkRes -> ok(this.value)
        is ErrRes -> block(error)
    }
}

public inline fun <Ok, Err, R> Res<Ok, Err>.mapOk(
    block: (value: Ok) -> R
): Res<R, Err> {
    return when (this) {
        is OkRes -> ok(block(value))
        is ErrRes -> err(error)
    }
}

@OptIn(ExperimentalContracts::class)
public inline fun <R> runCatchingRes(
    block: () -> R
): Res<R, Throwable> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return try {
        ok(block())
    } catch (e: Throwable) {
        err(e)
    }
}
