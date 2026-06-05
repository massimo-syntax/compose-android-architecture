package com.example.hiltretrofit.data.remote.interceptors

import com.example.hiltretrofit.data.remote.MockServer
import jakarta.inject.Inject
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody


class MockInterceptor @Inject constructor(private val server: MockServer) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val parameter = chain.request().url.pathSegments.last()

        val res = server.routeResponse(parameter)

        val response = Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("success")
            .header("MOCKED_HEADER_KEY", "MOCKED_HEADER_VALUE")
            .body(
                res.toResponseBody("application/json".toMediaTypeOrNull())
            )
            .build()

        return response
    }
}