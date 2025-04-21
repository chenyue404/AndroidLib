package com.chenyue404.androidlib.extends

import android.util.Log
import com.chenyue404.androidlib.logcat.L
import com.chenyue404.androidlib.logcat.LogCatPriority

/**
 * 打印日志。
 *
 * 默认会带一行堆栈信息，一般情况下是打log的位置
 * @param msg 日志
 * @param tag 标签
 * @param level 日志 [LogCatPriority]
 * @param showStackInfo 是否打印堆栈信息
 * @param stackLines 堆栈信息行数
 */
fun Any.log(
    msg: String,
    tag: String = "",
    level: LogCatPriority = LogCatPriority.DEBUG,
    showStackInfo: Boolean = true,
    stackLines: Int = 1,
) {
    val finalTag = tag.ifEmpty { outerClassSimpleNameInternalOnlyDoNotUseKThxBye() }

    val sb = StringBuilder(msg)
    if (showStackInfo) {
        val throwableStr = Log.getStackTraceString(Throwable())
        val finalStackInfo = with((throwableStr).split("\n")) {
            subList(
                3,
                (3 + stackLines).coerceAtMost(size - 1)
            ).joinToString("\n")
        }
        sb.append("\n").append(finalStackInfo)
    }
    L.print(level, String(sb), finalTag)
}

private fun Any.outerClassSimpleNameInternalOnlyDoNotUseKThxBye(): String {
    val javaClass = this::class.java
    val fullClassName = javaClass.name
    val outerClassName = fullClassName.substringBefore('$')
    val simplerOuterClassName = outerClassName.substringAfterLast('.')
    return if (simplerOuterClassName.isEmpty()) {
        fullClassName
    } else {
        simplerOuterClassName.removeSuffix("Kt")
    }
}

/**
 * 打印Throwable
 */
fun Throwable.log() {
    L.e(Log.getStackTraceString(this)) { "" }
}