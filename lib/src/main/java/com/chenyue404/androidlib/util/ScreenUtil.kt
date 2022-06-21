package com.chenyue404.androidlib.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.view.WindowManager
import com.chenyue404.androidlib.ContextProvider

/**
 * 屏幕相关工具
 */
object ScreenUtil {
    /** 屏幕宽度 */
    fun getScreenWidth(): Int {
        val windowManager = ContextProvider.mContext
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        return size.x
    }

    /** 屏幕高度 */
    fun getScreenHeight(): Int {
        val windowManager = ContextProvider.mContext
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        return size.y
    }

    /** 获取导航栏的高度 */
    fun getNavBarHeight(): Int {
        val res: Resources = ContextProvider.mContext.resources
        val resourceId: Int = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /** 获取状态栏高度 */
    fun getStatusBarHeight(): Int {
        val resources: Resources = ContextProvider.mContext.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
}