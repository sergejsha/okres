package de.halfbit.okres

import de.halfbit.okres.ReadingService.Err
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ResTest {

    @Test
    fun onOk_calledForOkRes() {
        // given
        val expected = 20.ok
        val reader = ReadingService(readingResult = expected)

        // when
        val res = reader.read()

        // then
        var onOkCalled = false
        res.onOk { actual ->
            assertEquals(expected, actual)
            onOkCalled = true
        }
        assertTrue(onOkCalled)
    }

    @Test
    fun onErr_notCalledForOkRes() {
        // given
        val expected = 20.ok
        val reader = ReadingService(readingResult = expected)

        // when
        val res = reader.read()

        // then
        var onErrCalled = false
        res.onErr { _ ->
            onErrCalled = true
        }
        assertFalse(onErrCalled)
    }

    @Test
    fun onErr_calledForErrRes() {
        // given
        val expected = Err.NoConnection.err
        val reader = ReadingService(readingResult = expected)

        // when
        val res = reader.read()

        // then
        var onErrCalled = false
        res.onErr { actual ->
            assertEquals(expected, actual)
            onErrCalled = true
        }
        assertTrue(onErrCalled)
    }

    @Test
    fun onOk_notCalledForErrRes() {
        // given
        val expected = Err.UserNotLogged.err
        val reader = ReadingService(readingResult = expected)

        // when
        val res = reader.read()

        // then
        var onErrCalled = false
        res.onOk { _ ->
            onErrCalled = true
        }
        assertFalse(onErrCalled)
    }

    @Test
    fun andThen_onOkCalled() {
        // given
        val expected = Success.ok
        val reader = ReadingService(10.ok)
        val validator = ValidatingService(expected)

        // when
        var onOkCalled = false
        val actual =
            reader.read()
                .andThen { validator.validate(it) }
                .onOk { onOkCalled = true }

        // then
        assertTrue(onOkCalled)
        assertEquals(expected, actual)
    }

    @Test
    fun andThen_onErrCalled() {
        // given
        val expected = Error.err
        val reader = ReadingService(10.ok)
        val validator = ValidatingService(expected)

        // when
        var onErrCalled = false
        val actual =
            reader.read()
                .andThen { validator.validate(it) }
                .onErr { onErrCalled = true }

        // then
        assertTrue(onErrCalled)
        assertEquals(expected, actual)
    }

    @Test
    fun andThen_onErrCalled_whenFirstServiceReturnsError() {
        // given
        val expected = Err.NoConnection.err
        val reader = ReadingService(expected)
        val validator = ValidatingService(Success.ok)

        // when
        var actual: ErrRes<Any>? = null
        reader.read()
                .andThen { validator.validate(it) }
                .onErr { actual = it }

        // then
        assertEquals(expected, actual)
        assertFalse(validator.called)
    }

    @Test
    fun andThen_onErrCalled_whenSecondServiceReturnsError() {
        // given
        val expected = Error.err
        val reader = ReadingService(10.ok)
        val validator = ValidatingService(expected)

        // when
        var actual: ErrRes<Any>? = null
        reader.read()
                .andThen { validator.validate(it) }
                .onErr { actual = it }

        // then
        assertEquals(expected, actual)
    }
}

private class ReadingService(
    private val readingResult: Res<Int, Err>,
) {
    var called = false

    sealed interface Err {
        data object NoConnection : Err
        data object UserNotLogged : Err
    }

    fun read(): Res<Int, Err> {
        called = true
        return readingResult
    }
}

private class ValidatingService(
    private val validatingResult: Res<Success, Error>,
) {
    var called = false

    fun validate(value: Int): Res<Success, Error> {
        called = true
        return validatingResult
    }
}
