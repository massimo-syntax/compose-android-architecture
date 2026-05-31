package com.example.mock_interceptor_retrofit.remote.retrofilt

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody


class MockInterceptor : Interceptor {

    private fun createString( special:Any? = "nothing"):String{
        val chainProperties: String = special?.toString() ?: "was null"

        val responseString = """
            [
                {"id": 1, "title": "$chainProperties", "body": "text of first jay@example.com"},
                {"id": 2, "title": "Kishan", "body": "$special"}
            ]
        """.trimIndent()

        return responseString
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request().url
        val encodedPath = url.encodedPath

        val responseString = createString("$url -- $encodedPath ")

        return Response.Builder()
            .code(200)
            .message(responseString)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(
                responseString.toByteArray()
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }
}