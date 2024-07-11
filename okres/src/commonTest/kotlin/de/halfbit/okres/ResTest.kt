package de.halfbit.okres

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ResTest {

    @Test
    fun onOkIsCalledForOkRes() {
        // given
        val expected = OkRes<Int, Reader.Err>(20)
        val reader = Reader(value = expected)

        // when
        val res = reader.readValue()

        // then
        var onOkCalled = false
        res.onOk { actual ->
            assertEquals(expected, actual)
            onOkCalled = true
        }
        assertTrue(onOkCalled)
    }

    @Test
    fun onErrIsNotCalledForOkRes() {
        // given
        val expected = OkRes<Int, Reader.Err>(20)
        val reader = Reader(value = expected)

        // when
        val res = reader.readValue()

        // then
        var onErrCalled = false
        res.onErr { _ ->
            onErrCalled = true
        }
        assertFalse(onErrCalled)
    }

    @Test
    fun onErrIsCalledForErrRes() {
        // given
        val expected = ErrRes<Int, Reader.Err>(Reader.Err.NoConnection)
        val reader = Reader(value = expected)

        // when
        val res = reader.readValue()

        // then
        var onErrCalled = false
        res.onErr { actual ->
            assertEquals(expected, actual)
            onErrCalled = true
        }
        assertTrue(onErrCalled)
    }

    @Test
    fun onOkIsNotCalledForErrRes() {
        // given
        val expected = ErrRes<Int, Reader.Err>(Reader.Err.UserNotLogged)
        val reader = Reader(value = expected)

        // when
        val res = reader.readValue()

        // then
        var onErrCalled = false
        res.onOk { _ ->
            onErrCalled = true
        }
        assertFalse(onErrCalled)
    }
}

private class Reader(
    private val value: Res<Int, Err>,
) {

    sealed interface Err {
        data object NoConnection : Err
        data object UserNotLogged : Err
    }

    fun readValue(): Res<Int, Err> = value
}
