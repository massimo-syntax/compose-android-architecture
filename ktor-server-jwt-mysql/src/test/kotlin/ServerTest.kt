package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ServerTest {

    @Test
    fun `test root endpoint`() = testApplication {
        configure()
        assertEquals(HttpStatusCode.OK, client.get("/").status)
    }

    @Test
    fun `test public blog route without auth`() = testApplication {
        configure()
        val response: HttpResponse = client.get("/blogs/all")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
