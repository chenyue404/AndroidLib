package com.chenyue404.androidlib.util.json

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class BooleanTypeAdapter : TypeAdapter<Boolean>() {
    override fun write(out: JsonWriter, value: Boolean?) {
        out.value(value)
    }

    override fun read(`in`: JsonReader): Boolean? {
        return when (`in`.peek()) {
            JsonToken.BOOLEAN -> `in`.nextBoolean()
            JsonToken.STRING ->                 // 如果后台返回 "true" 或者 "TRUE"，则处理为 true，否则为 false
                java.lang.Boolean.parseBoolean(`in`.nextString())
            JsonToken.NUMBER ->                 // 如果这个后台返回是 1 则处理为 true，否则为 false
                `in`.nextInt() == 1
            JsonToken.NULL -> {
                `in`.nextNull()
                null
            }
            else -> {
                `in`.skipValue()
                null
            }
        }
    }
}