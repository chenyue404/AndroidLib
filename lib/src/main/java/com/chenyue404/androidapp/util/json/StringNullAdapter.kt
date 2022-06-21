package com.chenyue404.androidapp.util.json

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class StringNullAdapter : TypeAdapter<String>() {
    override fun write(out: JsonWriter, value: String?) {
        out.value(value ?: "")
    }

    override fun read(jsonReader: JsonReader): String {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull()
            return ""
        }
        return jsonReader.nextString()
    }
}