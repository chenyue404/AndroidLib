package com.chenyue404.androidapp

import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Lifecycle
import com.chenyue404.androidlib.extends.*
import com.chenyue404.androidlib.widget.BaseActivity

class MainActivity : BaseActivity() {

    private val bt0 by bind<Button>(R.id.bt0)
    private val bt1 by bind<Button>(R.id.bt1)

    override fun getContentViewResId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bt0.click {
            it.changeSize(it.height * 2)
            it.requestLayout()
        }
        bt1.click(false) {
            it.changeSize(it.height * 2)
            it.requestLayout()
        }

        lifecycle.launch(Lifecycle.State.RESUMED) {
            log("RESUMED-false")
        }
        lifecycle.launch(Lifecycle.State.RESUMED, true) {
            log("RESUMED-true")
        }
    }
}