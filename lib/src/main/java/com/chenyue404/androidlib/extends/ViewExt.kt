package com.chenyue404.androidlib.extends

import android.app.Activity
import android.app.Dialog
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText

class FastClickListener(
    private val time: Int = BLOCKING_OF_TIME,
    val action: (View) -> Unit
) : View.OnClickListener {
    private var timeStamp = 0L
    override fun onClick(view: View) {
        val clickTimeStamp = System.currentTimeMillis()
        if (clickTimeStamp - timeStamp < time) return
        action(view)
        timeStamp = clickTimeStamp
    }

    companion object {
        const val BLOCKING_OF_TIME = 300
    }
}

/**
 * 给View设置点击事件
 * @param denyFastClick 是否防止快速点击，默认为true
 * @param interval 快速点击的间隔
 */
fun View.click(
    denyFastClick: Boolean = true,
    interval: Int = FastClickListener.BLOCKING_OF_TIME,
    block: (View) -> Unit
) = setOnClickListener(
    if (denyFastClick) FastClickListener(interval, block)
    else View.OnClickListener(block)
)

/**
 * 改变View的可见性
 * @param visible true为[View.VISIBLE], false为[View.GONE]
 */
fun View.visible(visible: Boolean? = true) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}

/**
 * 将View的可见性设置为[View.GONE]
 */
fun View.gone() {
    visibility = View.GONE
}

fun <V : View> Activity.bind(id: Int): Lazy<V> = lazy { findViewById(id) }

//fun <V : View> BaseFragment.bind(id: Int): Lazy<V> = lazy { findViewById(id) }
fun <V : View> View.bind(id: Int): Lazy<V> = lazy { findViewById(id) }
fun <V : View> Dialog.bind(id: Int): Lazy<V> = lazy { findViewById(id) }

private const val InputType_PSW_Hide =
    EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD // 129

private const val InputType_PSW_Visible =
    EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD //145

/**
 * 设置EditText的密码可见性
 */
fun EditText.setPasswordVisibility(setVisible: Boolean) {
    if (inputType == InputType_PSW_Hide || inputType == InputType_PSW_Visible) {
        inputType = if (setVisible) InputType_PSW_Visible else InputType_PSW_Hide
    } else {
        transformationMethod =
            if (setVisible) HideReturnsTransformationMethod.getInstance()
            else PasswordTransformationMethod.getInstance()
    }
    setSelection(text.toString().length)
}

/**
 * 修改view的宽高
 * @param newHeight 新的高度，不大于0则忽略，默认-1
 * @param newWidth 新的宽度，不大于0则忽略，默认-1
 */
fun View.changeSize(newHeight: Int = -1, newWidth: Int = -1) {
    this.layoutParams.apply {
        if (newHeight > 0) height = newHeight
        if (newWidth > 0) height = newWidth
    }
}