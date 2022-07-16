package com.chenyue404.androidlib.widget

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    protected val mContext: Context
        get() = this
    protected var rootView: View? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        initBeforeSetContent()
        super.onCreate(savedInstanceState)
        bindContentViewRes()
        initView()
    }

    private fun bindContentViewRes() {
        if (getContentViewResId() != 0) {
            rootView = LayoutInflater.from(mContext).inflate(getContentViewResId(), null)
            setContentView(rootView)
        }
    }

    @LayoutRes
    protected abstract fun getContentViewResId(): Int

    protected fun initBeforeSetContent() {}
    abstract fun initView()

    val toolbar: ToolbarView by lazy {
        ToolbarView(this).apply {
            attachToolbar(this@BaseActivity.rootView)
        }
    }
}