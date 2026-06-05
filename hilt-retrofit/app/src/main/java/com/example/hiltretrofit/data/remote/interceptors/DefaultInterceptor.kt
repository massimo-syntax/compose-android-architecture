package com.example.hiltretrofit.data.remote.interceptors

import com.example.hiltretrofit.data.remote.choices
import jakarta.inject.Inject
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class DefaultInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        // taken from: https://www.javathinking.com/blog/how-to-change-body-in-okhttp-response/

        // Proceed with the original request to get the response
        val originalRequest = chain.request()
        // *not needed*
        // val originalResponse = chain.proceed(originalRequest)
        // Create a new response with the modified body
        // val mediaType = originalResponse.body?.contentType() ?: "application/json".toMediaTypeOrNull()
        // val newBody = modifiedBody.toResponseBody(mediaType)

        val jsonResponseBody = choices

        val response = Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("success")
            .header("MOCKED_HEADER_KEY", "MOCKED_HEADER_VALUE")
            .body( jsonResponseBody.toResponseBody("application/json".toMediaTypeOrNull()))
            .build()

        return response
    }
}