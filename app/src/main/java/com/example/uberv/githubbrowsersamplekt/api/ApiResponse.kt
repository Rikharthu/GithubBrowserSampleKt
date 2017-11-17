package com.example.uberv.githubbrowsersamplekt.api

import android.support.annotation.Nullable
import android.util.ArrayMap
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.util.*
import java.util.regex.Pattern


class ApiResponse<T> {

    companion object {
        private val LINK_PATTERN = Pattern
                .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private val NEXT_LINK = "next"
    }

    var code: Int
    @Nullable
    var body: T?
    @Nullable
    var errorMessage: String?
    var links: MutableMap<String, String>

    constructor(error: Throwable) {
        code = 500
        body = null
        errorMessage = error.message
        links = Collections.emptyMap()
    }

    constructor (response: Response<T>) {
        code = response.code()
        if (response.isSuccessful()) {
            body = response.body()
            errorMessage = null
        } else {
            var message: String? = null
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody()!!.string()
                } catch (ignored: IOException) {
                    Timber.e(ignored, "error while parsing response")
                }

            }
            if (message == null || message.trim { it <= ' ' }.isEmpty()) {
                message = response.message()
            }
            errorMessage = message
            body = null
        }
        val linkHeader = response.headers().get("link")
        if (linkHeader == null) {
            links = Collections.emptyMap()
        } else {
            links = ArrayMap()
            val matcher = LINK_PATTERN.matcher(linkHeader)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links.put(matcher.group(2), matcher.group(1))
                }
            }
        }
    }

    fun isSuccessful(): Boolean {
        return code in 200..299
    }

    fun getNextPage(): Int? {
        val next = links[NEXT_LINK] ?: return null
        val matcher = PAGE_PATTERN.matcher(next)
        if (!matcher.find() || matcher.groupCount() != 1) {
            return null
        }
        return try {
            Integer.parseInt(matcher.group(1))
        } catch (ex: NumberFormatException) {
            Timber.w("cannot parse next page from %s", next)
            null
        }

    }
}