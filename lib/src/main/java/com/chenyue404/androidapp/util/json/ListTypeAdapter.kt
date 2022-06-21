package com.chenyue404.androidapp.util.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ListTypeAdapter : JsonDeserializer<List<Any>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<Any> {
        val newList = arrayListOf<Any>()
        if (json.isJsonArray) {
            val array = json.asJsonArray
            val itemType = (typeOfT as ParameterizedType).actualTypeArguments[0]
            array.forEach {
                newList.add(context.deserialize(it, itemType))
            }
        }
        return newList
    }
}