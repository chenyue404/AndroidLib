package com.chenyue404.androidlib.logcat

import android.util.Log
import com.chenyue404.androidlib.logcat.LogCat.defaultTag
import com.chenyue404.androidlib.logcat.LogCat.enabled
import com.chenyue404.androidlib.logcat.LogCat.logInterceptors
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.math.min

/** 日志等级 */
enum class LogCatPriority(
    val priorityInt: Int
) {
    VERBOSE(2),
    DEBUG(3),
    INFO(4),
    WARN(5),
    ERROR(6),
    ASSERT(7);
}

/**
 * @property defaultTag 默认标签
 * @property enabled 全局开关
 * @property logInterceptors 日志拦截器
 */
object LogCat {

    var defaultTag = "日志"
    var enabled = true

    val logInterceptors by lazy { arrayListOf<LogInterceptor>() }

    //<editor-fold desc="拦截器">
    /**
     * 添加日志拦截器
     */
    fun addInterceptor(interceptor: LogInterceptor) {
        logInterceptors.add(interceptor)
    }

    /**
     * 删除日志拦截器
     */
    fun removeInterceptor(interceptor: LogInterceptor) {
        logInterceptors.remove(interceptor)
    }

    //</editor-fold>

    // <editor-fold desc="输出">

    fun v(message: String?, tag: String? = this.defaultTag) {
        print(
            LogCatPriority.VERBOSE,
            message,
            tag
        )
    }

    fun i(message: String?, tag: String? = this.defaultTag) {
        print(
            LogCatPriority.INFO,
            message,
            tag
        )
    }

    fun d(message: String?, tag: String? = this.defaultTag) {
        print(
            LogCatPriority.DEBUG,
            message,
            tag
        )
    }

    fun w(message: String?, tag: String? = this.defaultTag) {
        print(
            LogCatPriority.WARN,
            message,
            tag
        )
    }

    fun e(message: String?, tag: String? = this.defaultTag) {
        print(
            LogCatPriority.ERROR,
            message,
            tag
        )
    }

    fun wtf(message: String?, tag: String? = this.defaultTag) {
        print(
            LogCatPriority.ASSERT,
            message,
            tag
        )
    }

    /**
     * 输出日志
     * 如果[message]为空或者[tag]为空不输出
     *
     * @param level 日志等级. [LogCatPriority]
     * @param message 日志信息
     * @param tag 日志标签
     */
    fun print(
        level: LogCatPriority = LogCatPriority.INFO,
        message: String? = null,
        tag: String? = defaultTag,
    ) {
        if (!enabled || tag.isNullOrEmpty()) return

        val chain = Chain(level, message, tag)

        logInterceptors.forEach {
            it.intercept(chain)
        }

        if (chain.cancel || tag.isEmpty()) return

        val adjustMsg = message ?: ""

        val max = 3800
        val length = adjustMsg.length
        if (length > max) {
            synchronized(this) {
                var startIndex = 0
                var endIndex = max
                while (startIndex < length) {
                    endIndex = min(length, endIndex)
                    val substring = adjustMsg.substring(startIndex, endIndex)
                    log(level, substring, tag)
                    startIndex += max
                    endIndex += max
                }
            }
        } else {
            log(level, adjustMsg, tag)
        }
    }

    /**
     * Json格式输出Log
     *
     * @param message JSON
     * @param tag     标签
     * @param url     地址
     */
    fun json(
        message: String?,
        tag: String? = this.defaultTag,
        url: String? = null,
        level: LogCatPriority = LogCatPriority.INFO
    ) {
        if (!enabled || tag.isNullOrBlank()) return

        val chain = Chain(level, message, tag)

        logInterceptors.forEach {
            it.intercept(chain)
        }

        if (chain.cancel || tag.isBlank()) return

        if (message.isNullOrBlank()) {
            val adjustMsg = if (url.isNullOrBlank()) message else url
            print(level, adjustMsg, tag)
            return
        }

        val tokener = JSONTokener(message)

        val obj = try {
            tokener.nextValue()
        } catch (e: Exception) {
            "Parse json error"
        }

        var finalMsg = when (obj) {
            is JSONObject -> {
                obj.toString(2)
            }
            is JSONArray -> {
                obj.toString(2)
            }
            else -> obj.toString()
        }

        if (!url.isNullOrBlank()) finalMsg = "$url\n$finalMsg"

        print(level, finalMsg, tag)
    }

    private fun log(level: LogCatPriority, adjustMsg: String, tag: String) {
        when (level) {
            LogCatPriority.VERBOSE -> {
                Log.v(tag, adjustMsg)
            }
            LogCatPriority.DEBUG -> {
                Log.d(tag, adjustMsg)
            }
            LogCatPriority.INFO -> {
                Log.i(tag, adjustMsg)
            }
            LogCatPriority.WARN -> {
                Log.w(tag, adjustMsg)
            }
            LogCatPriority.ERROR -> {
                Log.e(tag, adjustMsg)
            }
            LogCatPriority.ASSERT -> {
                Log.wtf(tag, adjustMsg)
            }
            else -> {
                Log.e(tag, adjustMsg)
            }
        }
    }
    // </editor-fold>
}