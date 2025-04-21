package com.chenyue404.androidlib.logcat

import android.util.Log
import com.chenyue404.androidlib.logcat.L.defaultTag
import com.chenyue404.androidlib.logcat.L.enabled
import com.chenyue404.androidlib.logcat.L.logInterceptors
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
object L {

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

    fun v(tag: String = defaultTag, throwable: Throwable? = null, msgFun: () -> String?) {
        print(LogCatPriority.VERBOSE, buildMsg(null, msgFun), tag)
    }

    fun d(tag: String = defaultTag, throwable: Throwable? = null, msgFun: () -> String?) {
        print(LogCatPriority.DEBUG, buildMsg(null, msgFun), tag)
    }

    fun i(tag: String = defaultTag, throwable: Throwable? = null, msgFun: () -> String?) {
        print(LogCatPriority.INFO, buildMsg(null, msgFun), tag)
    }

    fun w(tag: String = defaultTag, throwable: Throwable? = null, msgFun: () -> String?) {
        print(LogCatPriority.WARN, buildMsg(null, msgFun), tag)
    }

    fun e(tag: String = defaultTag, throwable: Throwable? = null, msgFun: () -> String?) {
        print(LogCatPriority.ERROR, buildMsg(null, msgFun), tag)
    }

    fun wtf(tag: String = defaultTag, throwable: Throwable? = null, msgFun: () -> String?) {
        print(LogCatPriority.ASSERT, buildMsg(null, msgFun), tag)
    }

    private fun buildMsg(throwable: Throwable? = null, msgFun: () -> String?): String {
        val msg = msgFun.invoke()?.takeIf { it.isNotEmpty() } ?: ""
        val sb = StringBuilder()
        val stackTrace = Throwable().stackTrace
        val targetElement = stackTrace.first {
            it.className != this.javaClass.name
        }
        var className = targetElement.className.substringAfterLast('.')
        val lineNumber = targetElement.lineNumber
        val threadName = Thread.currentThread().name

        if ('$' in className) {
            className = className.split('$')
                .filter { it.isNotEmpty() }
                .joinToString("$")
        }

        sb.append("[${System.currentTimeMillis()}]").append(" ")
            .append(className).append(":").append(lineNumber).append(" ")
            .append("($threadName)").append(" ")
            .append("> ").append(msg)
        throwable?.let {
            sb.append("\n")
                .append("Message: ").append(it.localizedMessage)
                .append("\nStacktrace : ").append(Log.getStackTraceString(it))
        }
        return sb.toString()
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
//            else -> {
//                Log.e(tag, adjustMsg)
//            }
        }
    }
    // </editor-fold>
}