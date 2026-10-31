package com.example.data.repository

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import com.example.data.mapper.EmailDetailsMapper
import com.example.data.mapper.EmailListMapper
import com.example.data.model.dto.emaildetails.EmailDetailsDto
import com.example.data.model.dto.emaildetails.PayloadDto
import com.example.data.model.dto.emaillist.EmailListItemDto
import com.example.data.remote.api.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class EmailRepositoryImplMockKTest {

    private val apiService: ApiService = mockk()
    private val emailListMapper = EmailListMapper()
    private val emailDetailsMapper = EmailDetailsMapper()

    private lateinit var repository: EmailRepositoryImpl

    @Before
    fun setUp() {
        repository = EmailRepositoryImpl(
            apiService = apiService,
            emailListMapper = emailListMapper,
            emailDetailsMapper = emailDetailsMapper
        )
    }

    @Test
    fun `getEmailList calls apiService and returns mapped domain model on success`() = runTest {
        // Given
        val dtoList = listOf(
            EmailListItemDto(
                id = "1",
                title = "Email 1",
                from = "user1@test.com",
                image = "img1.png",
                timestamp = 100L,
                favorite = false
            ),
            EmailListItemDto(
                id = "2",
                title = "Email 2",
                from = "user2@test.com",
                image = "img2.png",
                timestamp = 200L,
                favorite = true
            )
        )
        coEvery { apiService.getEmailList() } returns dtoList

        // When
        val result = repository.getEmailList()

        // Then
        coVerify(exactly = 1) { apiService.getEmailList() }
        assertTrue("Expected ResultEither.Success", result is ResultEither.Success)

        val items = (result as ResultEither.Success).value
        assertEquals(2, items.size)
        // Check sorting descending by timestamp
        assertEquals("2", items[0].id)
        assertEquals("Email 2", items[0].title)
        assertEquals("1", items[1].id)
    }

    @Test
    fun `getEmailList returns Failure NetworkError when apiService throws IOException`() = runTest {
        // Given
        val networkException = IOException("No network connection")
        coEvery { apiService.getEmailList() } throws networkException

        // When
        val result = repository.getEmailList()

        // Then
        coVerify(exactly = 1) { apiService.getEmailList() }
        assertTrue("Expected ResultEither.Error", result is ResultEither.Error)

        val error = (result as ResultEither.Error).value
        assertTrue("Expected Failure.NetworkError", error is Failure.NetworkError)
        assertEquals(networkException, (error as Failure.NetworkError).exception)
    }

    @Test
    fun `getEmailList returns Failure ServerError when apiService throws HttpException`() = runTest {
        // Given
        val httpResponse = Response.error<List<EmailListItemDto>>(
            500,
            "Internal Server Error".toResponseBody()
        )
        val httpException = HttpException(httpResponse)
        coEvery { apiService.getEmailList() } throws httpException

        // When
        val result = repository.getEmailList()

        // Then
        coVerify(exactly = 1) { apiService.getEmailList() }
        assertTrue("Expected ResultEither.Error", result is ResultEither.Error)

        val error = (result as ResultEither.Error).value
        assertTrue("Expected Failure.ServerError", error is Failure.ServerError)
        assertEquals(500, (error as Failure.ServerError).code)
    }

    @Test
    fun `getEmailDetail calls apiService and returns mapped details list on success`() = runTest {
        // Given
        val dtoList = listOf(
            EmailDetailsDto(
                id = "101",
                title = "Detail Title",
                body = "Detail Body",
                image = "image.png",
                payload = PayloadDto(
                    from = "sender",
                    email = "sender@email.com",
                    timestamp = 500L
                )
            )
        )
        coEvery { apiService.getEmailDetail() } returns dtoList

        // When
        val result = repository.getEmailDetail()

        // Then
        coVerify(exactly = 1) { apiService.getEmailDetail() }
        assertTrue("Expected ResultEither.Success", result is ResultEither.Success)

        val details = (result as ResultEither.Success).value
        assertEquals(1, details.size)
        assertEquals("101", details[0].id)
        assertEquals("Detail Title", details[0].title)
        assertEquals("Detail Body", details[0].body)
        assertEquals("sender@email.com", details[0].payload?.email)
    }

    @Test
    fun `getEmailDetail returns Failure UnknownError when unexpected Throwable is thrown`() = runTest {
        // Given
        val unexpectedException = IllegalStateException("Unexpected state")
        coEvery { apiService.getEmailDetail() } throws unexpectedException

        // When
        val result = repository.getEmailDetail()

        // Then
        coVerify(exactly = 1) { apiService.getEmailDetail() }
        assertTrue("Expected ResultEither.Error", result is ResultEither.Error)

        val error = (result as ResultEither.Error).value
        assertTrue("Expected Failure.UnknownError", error is Failure.UnknownError)
        assertEquals(unexpectedException, (error as Failure.UnknownError).throwable)
    }
}
