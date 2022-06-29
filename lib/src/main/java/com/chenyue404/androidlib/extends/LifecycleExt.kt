package com.chenyue404.androidlib.extends

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenStateAtLeast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 注册触发任务
 * @param state 触发任务的时机
 * @param repeat 是否重复触发
 * @param block 运行的任务
 */
fun Lifecycle.launch(
    state: Lifecycle.State = Lifecycle.State.CREATED,
    repeat: Boolean = false,
    block: suspend CoroutineScope.() -> Unit
) {
    coroutineScope.launch {
        if (repeat) {
            repeatOnLifecycle(state, block)
        } else {
            whenStateAtLeast(state, block)
        }
    }
}