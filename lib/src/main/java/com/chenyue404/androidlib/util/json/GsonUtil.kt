package com.chenyue404.androidlib.util.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Gson工具类
 */
object GsonUtil {

    val gson: Gson by lazy {
        GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Int::class.java, IntegerDefaultAdapter())
            .registerTypeAdapter(String::class.java, StringNullAdapter())
            .registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
            .registerTypeAdapter(Double::class.java, DoubleTypeAdapter())
            .registerTypeAdapter(Float::class.java, FloatTypeAdapter())
            .registerTypeAdapter(List::class.java, ListTypeAdapter())
            .registerTypeAdapter(Long::class.java, LongDefaultAdapter())
            .create()
    }

    fun toJson(paramObject: Any): String = gson.toJson(paramObject)

    fun <T> fromJson(json: String, type: Type): T {
        return gson.fromJson(json, type)
    }

    fun <T> fromJson(json: String): List<T> {
        return gson.fromJson(json, object : TypeToken<List<T>>() {}.type)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }

}