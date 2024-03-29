package com.marat.hvatit.playlistmaker2.domain.api

interface JsonParser {
    fun objectToJson(obj: Any): String

    fun <T> jsonToObject(json: String, classOfT: Class<T>): T
}