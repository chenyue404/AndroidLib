package com.chenyue404.androidapp

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import com.chenyue404.androidapp.extends.bind
import com.chenyue404.androidapp.extends.changeSize
import com.chenyue404.androidapp.extends.click

class MainActivity : Activity() {

    private val bt0 by bind<Button>(R.id.bt0)
    private val bt1 by bind<Button>(R.id.bt1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt0.click {
            it.changeSize(it.height * 2)
            it.requestLayout()
        }
        bt1.click(false) {
            it.changeSize(it.height * 2)
            it.requestLayout()
        }
    }
}