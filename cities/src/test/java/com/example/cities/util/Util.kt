package com.example.cities.util

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource

val dummyResponseBody = object : ResponseBody() {
    override fun contentLength(): Long {
        return 11
    }

    override fun contentType(): MediaType? {
        return null
    }

    override fun source(): BufferedSource {
        TODO("Not yet implemented")
    }
}