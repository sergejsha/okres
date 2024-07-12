package de.halfbit.okres

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ApiUsabilityTest {

    @Test
    fun workingWithOkValue() {
        val service = Service()

        val res = service
            .read(Success.ok)
            .andThen { service.validate(Success.ok) }

        when(res) {
            is OkRes -> assertEquals(Success.ok, res)
            is ErrRes -> assertFails { }
        }
    }


    @Test
    fun workingWithErrError() {
        val service = Service()

        val res = service
            .read(Success.ok)
            .andThen { service.validate(Error.err) }

        when(res) {
            is OkRes -> assertFails { }
            is ErrRes -> assertEquals(Error.err, res)
        }
    }

    @Test
    fun workingWithOnRes() {
        val service = Service()

        service
            .read(Success.ok)
            .andThen { service.validate(Success.ok) }
            .onRes(
                onOk = { value -> assertEquals(Success, value) },
                onErr = { assertFails { }  }
            )
    }
}

private class Service {
    fun read(result: Res<Success, Error>): Res<Success, Error> = result
    fun validate(result: Res<Success, Error>): Res<Success, Error> = result
}