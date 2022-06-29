package com.chenyue404.androidlib.widget

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    protected val mContext: Context
        get() = this

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentViewRes()
    }

    private fun bindContentViewRes() {
        if (getContentViewResId() != 0) {
            setContentView(getContentViewResId())
        }
    }

    @LayoutRes
    protected abstract fun getContentViewResId(): Int
}