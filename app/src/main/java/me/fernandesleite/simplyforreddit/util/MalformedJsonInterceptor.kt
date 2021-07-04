package me.fernandesleite.simplyforreddit.util

import com.jayway.jsonpath.JsonPath
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody


class MalformedJsonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        return if (response.body() == null) {
            response
        } else {
            updateResponse(response, cleanJson(response.body()!!.string())!!)
        }
    }

    private val iconElement = "$..icon_img"

    private fun cleanJson(jsonString: String?): String? {
        return JsonPath.parse(jsonString)
            .set(iconElement, "")
            .jsonString()
    }

    private fun updateResponse(previous: Response, newContent: String): Response {
        val contentType: MediaType = previous.body()!!.contentType()!!
        val body: ResponseBody = ResponseBody.create(contentType, newContent)
        return previous.newBuilder().body(body).build()
    }


}