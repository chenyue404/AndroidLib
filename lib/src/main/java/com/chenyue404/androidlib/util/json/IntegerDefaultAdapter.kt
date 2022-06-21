package com.chenyue404.androidlib.util.json

import com.google.gson.*
import java.lang.reflect.Type

class IntegerDefaultAdapter : JsonSerializer<Int>, JsonDeserializer<Int> {
    override fun serialize(
        src: Int?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src)
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Int {
        return json?.let {
            try {
                if (it.asString == "" || it.asString == "null") {
                    0
                } else it.asInt
            } catch (e: NumberFormatException) {
                0
            } catch (e: UnsupportedOperationException) {
                0
            }
        } ?: 0
    }
}