package com.chenyue404.androidapp

import android.graphics.Color
import android.widget.Button
import androidx.lifecycle.Lifecycle
import com.chenyue404.androidlib.extends.bind
import com.chenyue404.androidlib.extends.changeSize
import com.chenyue404.androidlib.extends.click
import com.chenyue404.androidlib.extends.dp2Px
import com.chenyue404.androidlib.extends.launch
import com.chenyue404.androidlib.logcat.FileLogInterceptor
import com.chenyue404.androidlib.logcat.L
import com.chenyue404.androidlib.widget.BaseActivity
import com.chenyue404.androidlib.widget.ToolbarView

class MainActivity : BaseActivity() {

    private val bt0 by bind<Button>(R.id.bt0)
    private val bt1 by bind<Button>(R.id.bt1)

    override fun getContentViewResId() = R.layout.activity_main
    override fun initView() {
        L.addInterceptor(
            FileLogInterceptor(
                this,
                (externalCacheDir ?: cacheDir).absolutePath,
                "log",
                shouldRoll = {
                    (it?.length() ?: 0) > 1024 * 1024
                }
            )
        )
        toolbar.apply {
            setBackgroundColor(Color.GREEN)
            alpha = 0.5f
            start(
                "asd",
                R.style.LeftText,
                ToolbarView.defaultLayoutParams.apply {
                    marginStart = 10.dp2Px()
                }
            )
            center("center")
            end(android.R.drawable.ic_menu_close_clear_cancel)
        }

        bt0.click {
            it.changeSize(it.height * 2)
            it.requestLayout()
        }
        bt1.click(false) {
            it.changeSize(it.height * 2)
            it.requestLayout()
        }

        lifecycle.launch(Lifecycle.State.RESUMED) {
//            log("RESUMED-false")
            L.d { "RESUMED-false" }
        }
        lifecycle.launch(Lifecycle.State.RESUMED, true) {
//            log("RESUMED-true")
            L.d { "RESUMED-true" }
        }
    }

    override fun initBeforeSetContent() {
    }
}