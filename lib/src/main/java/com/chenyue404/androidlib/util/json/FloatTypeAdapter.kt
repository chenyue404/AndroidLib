package com.chenyue404.androidlib.util.json

import com.google.gson.stream.JsonReader

class FloatTypeAdapter : DoubleTypeAdapter() {
    override fun read(jr: JsonReader): Number {
        val number = super.read(jr)
        return try {
            number.toFloat()
        } catch (e: NumberFormatException) {
            0
        }
    }
}