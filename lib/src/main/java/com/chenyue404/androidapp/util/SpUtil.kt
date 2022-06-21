package com.chenyue404.androidapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.chenyue404.androidapp.ContextProvider
import com.chenyue404.androidapp.util.SpUtil.Companion.setDefaultFileName

/**
 * SharedPreferences的相关工具
 * @param fileName sp的文件名，默认为"SharedPreferences"，可以通过[setDefaultFileName]修改
 */
class SpUtil(var fileName: String? = _defaultFileName) {

    companion object {
        private var _defaultFileName = "SharedPreferences"

        /**
         * 设置默认文件名
         */
        fun setDefaultFileName(fileName: String) {
            _defaultFileName = fileName
        }
    }

    private val prefs: SharedPreferences
        get() = ContextProvider.mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    /**
     * 放入元素
     * @param key 键名
     * @param value 元素, 支持Int, String, Long, Boolean, Float, Set<String>
     */
    fun put(key: String, value: Any?) {
        prefs.edit(commit = true) {
            when (value) {
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                is Long -> putLong(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Set<*> -> putStringSet(key, value as Set<String>)
                else -> putString(key, value.toString())
            }
        }
    }

    /**
     * 移除元素
     * @param key 键名
     */
    fun remove(key: String) {
        prefs.edit(commit = true) {
            remove(key)
        }
    }

    /**
     * 获取元素
     * @param key 键名
     * @param defaultValue 默认值, 支持Int, String, Long, Boolean, Float, Set<String>
     */
    fun <T> get(key: String, defaultValue: T?): T {
        return when (defaultValue) {
            is Int -> prefs.getInt(key, defaultValue)
            is String -> prefs.getString(key, defaultValue)
            is Long -> prefs.getLong(key, defaultValue)
            is Boolean -> prefs.getBoolean(key, defaultValue)
            is Float -> prefs.getFloat(key, defaultValue)
            is Set<*> -> prefs.getStringSet(key, defaultValue as Set<String>)
            else -> prefs.getString(key, defaultValue.toString())
        } as T
    }

    /**
     * 清空所有元素
     */
    fun clear() {
        prefs.edit(commit = true) {
            clear()
        }
    }

}
