package com.example.data.repository

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import com.example.data.mapper.EmailDetailsMapper
import com.example.data.mapper.EmailListMapper
import com.example.data.remote.api.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class EmailRepositoryImplMockWebServerTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService
    private lateinit var repository: EmailRepositoryImpl

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)

        repository = EmailRepositoryImpl(
            apiService = apiService,
            emailListMapper = EmailListMapper(),
            emailDetailsMapper = EmailDetailsMapper()
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getEmailList returns Success with mapped and sorted domain list on HTTP 200`() = runTest {
        // Given
        val jsonResponse = """
            [
                {
                    "id": "1",
                    "title": "Welcome Email",
                    "from": "alice@example.com",
                    "image": "https://example.com/a.png",
                    "timestamp": 1000,
                    "favorite": false
                },
                {
                    "id": "2",
                    "title": "Urgent Update",
                    "from": "bob@example.com",
                    "image": "https://example.com/b.png",
                    "timestamp": 2000,
                    "favorite": true
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
        )

        // When
        val result = repository.getEmailList()

        // Then
        assertTrue("Expected ResultEither.Success", result is ResultEither.Success)
        val successValue = (result as ResultEither.Success).value

        assertEquals(2, successValue.size)
        // Verify sorted descending by timestamp
        assertEquals("2", successValue[0].id)
        assertEquals("Urgent Update", successValue[0].title)
        assertEquals(true, successValue[0].favorite)
        assertEquals("1", successValue[1].id)

        // Verify correct API endpoint was requested
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("/api/v1/emaillist", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun `getEmailList filters out items with null id or null from`() = runTest {
        // Given - 1 valid item, 1 item with null id, 1 item with null from
        val jsonResponse = """
            [
                {
                    "id": "1",
                    "title": "Valid Email",
                    "from": "alice@example.com",
                    "image": null,
                    "timestamp": 1000,
                    "favorite": null
                },
                {
                    "id": null,
                    "title": "Invalid Email 1",
                    "from": "bob@example.com",
                    "image": null,
                    "timestamp": 2000,
                    "favorite": null
                },
                {
                    "id": "3",
                    "title": "Invalid Email 2",
                    "from": null,
                    "image": null,
                    "timestamp": 3000,
                    "favorite": null
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
        )

        // When
        val result = repository.getEmailList()

        // Then
        assertTrue("Expected ResultEither.Success", result is ResultEither.Success)
        val list = (result as ResultEither.Success).value
        assertEquals(1, list.size)
        assertEquals("1", list[0].id)
    }

    @Test
    fun `getEmailList returns ServerError on HTTP 500`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        // When
        val result = repository.getEmailList()

        // Then
        assertTrue("Expected ResultEither.Error", result is ResultEither.Error)
        val error = (result as ResultEither.Error).value
        assertTrue("Expected Failure.ServerError", error is Failure.ServerError)
        assertEquals(500, (error as Failure.ServerError).code)
    }

    @Test
    fun `getEmailList returns ServerError on HTTP 404`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("Not Found")
        )

        // When
        val result = repository.getEmailList()

        // Then
        assertTrue("Expected ResultEither.Error", result is ResultEither.Error)
        val error = (result as ResultEither.Error).value
        assertTrue("Expected Failure.ServerError", error is Failure.ServerError)
        assertEquals(404, (error as Failure.ServerError).code)
    }

    @Test
    fun `getEmailList returns NetworkError on network disconnect`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        )

        // When
        val result = repository.getEmailList()

        // Then
        assertTrue("Expected ResultEither.Error", result is ResultEither.Error)
        val error = (result as ResultEither.Error).value
        assertTrue("Expected Failure.NetworkError", error is Failure.NetworkError)
    }

    @Test
    fun `getEmailDetail returns Success with mapped email details on HTTP 200`() = runTest {
        // Given
        val jsonResponse = """
            [
                {
                    "id": "101",
                    "title": "Email Detail Subject",
                    "body": "Hello World Body",
                    "image": "https://example.com/img.png",
                    "payload": {
                        "from": "sender@example.com",
                        "email": "receiver@example.com",
                        "timestamp": 123456789
                    }
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
        )

        // When
        val result = repository.getEmailDetail()

        // Then
        assertTrue("Expected ResultEither.Success", result is ResultEither.Success)
        val details = (result as ResultEither.Success).value

        assertEquals(1, details.size)
        assertEquals("101", details[0].id)
        assertEquals("Email Detail Subject", details[0].title)
        assertEquals("Hello World Body", details[0].body)
        assertEquals("sender@example.com", details[0].payload?.from)

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("/api/v1/emaildetails", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }

    @Test
    fun `getEmailDetail returns ServerError on HTTP 500`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Server Error")
        )

        // When
        val result = repository.getEmailDetail()

        // Then
        assertTrue("Expected ResultEither.Error", result is ResultEither.Error)
        val error = (result as ResultEither.Error).value
        assertTrue("Expected Failure.ServerError", error is Failure.ServerError)
    }
}
