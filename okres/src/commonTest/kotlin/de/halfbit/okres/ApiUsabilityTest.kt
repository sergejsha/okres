package de.halfbit.okres

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ApiUsabilityTest {

    @Test
    fun workingWithOkValue() {
        val service = Service()

        val res = service
            .read(Success.asOk)
            .andThen { service.validate(Success.asOk) }

        when (res) {
            is OkRes -> assertEquals(Success.asOk, res)
            is ErrRes -> assertFails { }
        }
    }


    @Test
    fun workingWithErrError() {
        val service = Service()

        val res = service
            .read(Success.asOk)
            .andThen { service.validate(Error.asErr) }

        when (res) {
            is OkRes -> assertFails { }
            is ErrRes -> assertEquals(Error.asErr, res)
        }
    }

    @Test
    fun workingWithOnRes() {
        val service = Service()

        service
            .read(Success.asOk)
            .andThen { service.validate(Success.asOk) }
            .onRes(
                onOk = { value -> assertEquals(Success, value) },
                onErr = { assertFails { } }
            )
    }

    @Test
    fun runCatchingRes_ReturnsError() {
        val service = Service()

        val res: Res<Success, Error> =
            runCatchingRes {
                service.send(shouldThrow = true)
                Success
            }

        assertEquals(Error.asErr, res)
    }

    @Test
    fun mapRes() {
        val service = Service()

        val actual =
            service.read(Success.asOk)
                .mapRes(
                    onOk = { "success" },
                    onErr = { "error" }
                )

        assertEquals("success", actual)
    }
}

private class Service {
    fun read(result: Res<Success, Error>): Res<Success, Error> = result
    fun validate(result: Res<Success, Error>): Res<Success, Error> = result
    fun send(shouldThrow: Boolean) {
        if (shouldThrow) throw Exception("kaboom")
    }
}
