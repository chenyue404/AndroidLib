package com.chenyue404.androidlib.util.json

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

open class DoubleTypeAdapter : TypeAdapter<Number>() {
    override fun write(out: JsonWriter, value: Number?) {
        out.value(value)
    }

    override fun read(jr: JsonReader): Number {
        val zero = 0
        return when (jr.peek()) {
            JsonToken.NUMBER ->
                try {
                    jr.nextDouble()
                } catch (e: NumberFormatException) {
                    zero
                }
            JsonToken.STRING ->
                try {
                    jr.nextString().toDouble()
                } catch (e: NumberFormatException) {
                    zero
                }
            JsonToken.NULL -> {
                jr.nextNull()
                zero
            }
            else -> zero
        }
    }
}