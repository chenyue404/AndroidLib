package com.chenyue404.androidlib.extends

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

/**
 * 显示输入法
 */
fun EditText.showInputMethod() {
    try {
        val imeOptions =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//        imeOptions?.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        imeOptions?.showSoftInput(this, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 隐藏输入法
 */
fun Activity.hideInputMethod() {
    try {
        val imeOptions =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (imeOptions != null && imeOptions.isActive && null != currentFocus) {
            imeOptions.hideSoftInputFromWindow(
                currentFocus!!.applicationWindowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 隐藏输入法
 */
fun Fragment.hideInputMethod() {
    requireActivity().let {
        try {
            val imeOptions =
                it.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            if (imeOptions != null && imeOptions.isActive && null != it.currentFocus) {
                imeOptions.hideSoftInputFromWindow(
                    it.currentFocus!!.applicationWindowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}