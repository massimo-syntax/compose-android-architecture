package com.example.mock_interceptor_retrofit.remote.retrofilt

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

object MyMockServer{
    fun routeResponse( path:String = "nothing") : String =
        """
            [
                {"id": 1, "title": "$path", "body": "that was the path"},
                {"id": 2, "title": "path from url", "body": "$path"}
            ]
        """.trimIndent()
}

class MockInterceptor(
    private val server : MyMockServer
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
        val encodedPath = url.encodedPath
        val response = server.routeResponse("$url -- $encodedPath ")
        return Response.Builder()
            .code(200)
            .message(response)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(
                response.toByteArray()
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }
}