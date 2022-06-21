package com.chenyue404.androidlib.logcat

/**
 * 日志拦截器的链式对象
 *
 * @param level 等级
 * @param message 信息
 * @param tag 标签
 * @param cancel 是否终止
 */
data class Chain(
    var level: LogCatPriority,
    var message: String?,
    var tag: String,
    internal var cancel: Boolean = false
) {
    /**
     * 取消日志打印
     */
    fun cancel() {
        cancel = true
    }
}