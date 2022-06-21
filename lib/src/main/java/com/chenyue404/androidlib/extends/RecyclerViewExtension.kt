package com.chenyue404.androidlib.extends

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

/**
 * 给RecyclerView增加点击和长按事件
 */
fun RecyclerView.setOnItemClick(
    lifecycle: Lifecycle,
    itemClick: (recyclerView: RecyclerView, position: Int, view: View) -> Unit,
    itemLongClick: ((recyclerView: RecyclerView, position: Int, view: View) -> Unit)? = null,
) {
    val onChildAttachStateChangeListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            val holder = getChildViewHolder(view)
            view.setOnClickListener {
                itemClick(this@setOnItemClick, holder.adapterPosition, it)
            }
            itemLongClick?.let {
                view.setOnLongClickListener { view ->
                    it(this@setOnItemClick, holder.adapterPosition, view)
                    true
                }
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
        }
    }
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                removeOnChildAttachStateChangeListener(
                    onChildAttachStateChangeListener
                )
            }
        }
    })
    addOnChildAttachStateChangeListener(onChildAttachStateChangeListener)
}