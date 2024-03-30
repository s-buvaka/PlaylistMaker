package com.marat.hvatit.playlistmaker2.data.dto

import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.domain.api.JsonParser

class JsonParserImpl : JsonParser {
    private val gson = Gson()

    override fun objectToJson(obj: Any): String {
        return gson.toJson(obj)
    }

    override fun <T> jsonToObject(json: String, classOfT: Class<T>): T {
        return gson.fromJson(json, classOfT)
    }

}