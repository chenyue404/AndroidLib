package com.chenyue404.androidlib.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.chenyue404.androidlib.ContextProvider
import kotlin.math.abs

/** 软键盘相关工具类 */
object KeyboardUtil {
    private val imm
        get() = ContextProvider.mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    /** 显示软键盘 */
    fun showSoftInput() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /** 显示软键盘 */
    fun showSoftInput(activity: Activity) {
        if (!isSoftInputVisible(activity)) {
            toggleSoftInput()
        }
    }

    /**
     * 显示软键盘
     * @param view 目标View
     * @param flags Provides additional operating flags.  Currently may be
     * 0 or have the [InputMethodManager.SHOW_IMPLICIT] bit set.
     */
    fun showSoftInput(view: View, flags: Int = 0) {
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        imm.showSoftInput(view, flags, object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                    || resultCode == InputMethodManager.RESULT_HIDDEN
                ) {
                    toggleSoftInput()
                }
            }
        })
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 隐藏软键盘
     * @param activity 目标Activity
     */
    fun hideSoftInput(activity: Activity) {
        hideSoftInput(activity.window)
    }

    /**
     * 隐藏软键盘
     * @param window 目标Window.
     */
    fun hideSoftInput(window: Window) {
        var view: View? = window.currentFocus
        if (view == null) {
            val decorView: View = window.decorView
            val focusView = decorView.findViewWithTag<View>("keyboardTagView")
            if (focusView == null) {
                view = EditText(window.context)
                view.setTag("keyboardTagView")
                (decorView as ViewGroup).addView(view, 0, 0)
            } else {
                view = focusView
            }
            view.requestFocus()
        }
        hideSoftInput(view)
    }

    /**
     * 隐藏软键盘
     * @param view 目标view.
     */
    fun hideSoftInput(view: View) {
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private var millis: Long = 0

    /**
     * 隐藏软键盘
     * @param activity 目标Activity
     */
    fun hideSoftInputByToggle(activity: Activity) {
        val nowMillis: Long = SystemClock.elapsedRealtime()
        val delta = nowMillis - millis
        if (abs(delta) > 500 && isSoftInputVisible(activity)) {
            toggleSoftInput()
        }
        millis = nowMillis
    }

    /** 切换软键盘可见 */
    fun toggleSoftInput() {
        imm.toggleSoftInput(0, 0)
    }

    /**
     * 软键盘是否可见
     * @param activity 目标Activity
     */
    fun isSoftInputVisible(activity: Activity): Boolean {
        return getDecorViewInvisibleHeight(activity.window) > 0
    }

    private fun getDecorViewInvisibleHeight(window: Window): Int {
        val decorView = window.decorView
        val outRect = Rect()
        decorView.getWindowVisibleDisplayFrame(outRect)
        val delta: Int = abs(decorView.bottom - outRect.bottom)
        if (delta <= ScreenUtil.getNavBarHeight() + ScreenUtil.getStatusBarHeight()) {
            return 0
        }
        return delta
    }

}