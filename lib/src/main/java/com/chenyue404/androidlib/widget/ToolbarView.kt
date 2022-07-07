package com.chenyue404.androidlib.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.chenyue404.androidlib.extends.dp2Px
import com.chenyue404.androidlib.extends.drawable
import com.chenyue404.androidlib.util.ScreenUtil

class ToolbarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    companion object {
        val defaultLayoutParams: LayoutParams
            get() = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    private val defaultHeight = 56.dp2Px()
    private val initViews by lazy { mutableListOf<ViewGroup>() }
    var isImmerse: Boolean = true
        set(value) {
            field = value
            setPadding(0, if (value) ScreenUtil.getStatusBarHeight() else 0, 0, 0)
        }

    @DrawableRes
    var background: Int = 0
        set(value) {
            setBackground(drawable(value))
            field = value
        }

    private val idParent by lazy { generateViewId() }
    private val idLeft by lazy { generateViewId() }
    private val idCenter by lazy { generateViewId() }
    private val idRight by lazy { generateViewId() }
    private val idFullWidth by lazy { generateViewId() }

    init {
        id = idParent
        isImmerse = if (context is Activity) context.isImmersive else false
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        minHeight = defaultHeight
    }

    fun attachToolbar(root: View?) {
        parent?.run { return }
        root?.let {
            when (it) {
                is LinearLayout -> it.addView(this, 0)
                is FrameLayout -> it.addView(this)
                is ConstraintLayout -> {
                    (this@ToolbarView.layoutParams as LayoutParams).topToTop =
                        LayoutParams.PARENT_ID
                    val toolbarViewId = this@ToolbarView.id
                    it.children.forEach { child ->
                        val childLp = child.layoutParams as LayoutParams
                        childLp.run {
                            when {
                                topToTop == LayoutParams.PARENT_ID
                                        && bottomToTop == LayoutParams.PARENT_ID -> {
                                    topToBottom = toolbarViewId
                                    bottomToBottom = toolbarViewId
                                }
                                topToTop == LayoutParams.PARENT_ID -> {
                                    topToTop = -1
                                    topToBottom = toolbarViewId
                                }
                                bottomToTop == LayoutParams.PARENT_ID -> {

                                    bottomToTop = -1
                                    bottomToBottom = toolbarViewId
                                }
                            }
                            if (height == LayoutParams.MATCH_PARENT) {
                                height = 0
                                topToBottom = toolbarViewId
                                bottomToBottom = LayoutParams.PARENT_ID
                            }
                        }
                    }
                    it.addView(this)
                }
            }
        }
    }

    fun <T : View> left(view: View): T {
        val lp = view.layoutParams?.let {
            it as LayoutParams
        } ?: defaultLayoutParams
        addView(view,
            lp.apply {
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
                leftToLeft = LayoutParams.PARENT_ID
            })
        return view as T
    }

    fun left(
        @DrawableRes id: Int,
        @StyleRes style: Int = 0,
        lp: LayoutParams = defaultLayoutParams
    ): ImageView {
        val imageView =
            if (style == 0) ImageView(context)
            else ImageView(context, null, 0, style)
        imageView.apply {
            setImageResource(id)
            layoutParams = lp
        }
        return left(imageView)
    }

    fun left(
        str: CharSequence,
        @StyleRes style: Int = 0,
        lp: LayoutParams = defaultLayoutParams
    ): TextView {
        val textView =
            if (style == 0) TextView(context)
            else TextView(context, null, 0, style)
        textView.apply {
            text = str
            layoutParams = lp
        }
        return left(textView)
    }
}