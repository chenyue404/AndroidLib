package com.chenyue404.androidapp.logcat

/**
 * 日志拦截器
 */
interface LogInterceptor {

    /**
     * 拦截日志
     */
    fun intercept(chain: Chain)
}