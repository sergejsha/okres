package de.halfbit.okres

import de.halfbit.okres.ReadingService.Err
import kotlin.test.*

class ResTest {

    @Test
    fun onOk_called_whenServiceReturnsSuccess() {
        // given
        val expected = 20
        val reader = ReadingService(expected.asOk)

        // when
        var actual = 0
        reader.readValue().onOk { actual = it }

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun onErr_notCalled_whenServiceReturnsSuccess() {
        // given
        val expected = 20
        val reader = ReadingService(expected.asOk)

        // when
        var actual = 0
        var onErrCalled = false
        reader.readValue().onErr { onErrCalled = true }

        // then
        assertFalse(onErrCalled)
    }

    @Test
    fun onErr_called_whenServiceReturnsError() {
        // given
        val expected = Err.NoConnection
        val reader = ReadingService(expected.asErr)

        // when
        var actual: Err? = null
        reader.readValue().onErr { actual = it }

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun onOk_notCalled_whenServiceReturnsError() {
        // given
        val expected = Err.UserNotLogged
        val reader = ReadingService(expected.asErr)

        // when
        var onOkCalled = false
        reader.readValue().onOk { onOkCalled = true }

        // then
        assertFalse(onOkCalled)
    }

    @Test
    fun andThen_called_whenBothServicesReturnSuccess() {
        // given
        val expected = Success
        val reader = ReadingService(10.asOk)
        val validator = ValidatingService(expected.asOk)

        // when
        var actual1: Success? = null
        val actual2 =
            reader.readValue()
                .andThen { validator.validateValue(it) }
                .onOk { actual1 = it }

        // then
        assertEquals(expected, actual1)
        assertEquals(expected.asOk, actual2)
    }

    @Test
    fun andThen_onErrCalled() {
        // given
        val expected = Error.asErr
        val reader = ReadingService(10.asOk)
        val validator = ValidatingService(expected)

        // when
        var onErrCalled = false
        val actual =
            reader.readValue()
                .andThen { validator.validateValue(it) }
                .onErr { onErrCalled = true }

        // then
        assertTrue(onErrCalled)
        assertEquals(expected, actual)
    }

    @Test
    fun andThen_onErrCalled_whenFirstServiceReturnsError() {
        // given
        val expected = Err.NoConnection
        val reader = ReadingService(expected.asErr)
        val validator = ValidatingService(Success.asOk)

        // when
        var actual: Any? = null
        reader.readValue()
            .andThen { validator.validateValue(it) }
            .onErr { actual = it }

        // then
        assertEquals(expected, actual)
        assertFalse(validator.called)
    }

    @Test
    fun andThen_onErrCalled_whenSecondServiceReturnsError() {
        // given
        val expected = Error
        val reader = ReadingService(10.asOk)
        val validator = ValidatingService(expected.asErr)

        // when
        var actual: Any? = null
        reader.readValue()
            .andThen { validator.validateValue(it) }
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

    fun readValue(): Res<Int, Err> {
        called = true
        return readingResult
    }
}

private class ValidatingService(
    private val validatingResult: Res<Success, Error>,
) {
    var called = false

    fun validateValue(value: Int): Res<Success, Error> {
        called = true
        return validatingResult
    }
}
