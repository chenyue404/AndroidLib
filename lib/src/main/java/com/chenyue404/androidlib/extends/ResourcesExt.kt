package com.chenyue404.androidlib.extends

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Context.color(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Fragment.color(@ColorRes color: Int): Int {
    return context?.color(color) ?: run {
//        "get color fail, context is null".toastDebug()
        -1
    }
}

fun View.color(@ColorRes color: Int): Int {
    return context.color(color)
}

fun Context.drawable(@DrawableRes drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableId)
}

fun Fragment.drawable(@DrawableRes drawableId: Int): Drawable? {
    return context?.drawable(drawableId) ?: run {
//        "get drawable fail, context is null".toastDebug()
        null
    }
}

fun View.drawable(@DrawableRes drawableId: Int): Drawable? {
    return context.drawable(drawableId)
}

fun Context.dimen(@DimenRes dimen: Int): Int {
    return resources.getDimensionPixelSize(dimen)
}

fun Fragment.dimen(@DimenRes dimen: Int): Int {
    return context?.dimen(dimen) ?: run {
//        "get dimen fail, context is null".toastDebug()
        0
    }
}

fun View.dimen(@DimenRes dimen: Int): Int {
    return context.dimen(dimen)
}

fun Context.string(@StringRes resId: Int): String {
    return resources.getString(resId)
}

fun Context.string(@StringRes resId: Int, vararg items: Any): String {
    return resources.getString(resId, *items)
}

fun Context.stringArray(@ArrayRes resId: Int): Array<String> {
    return resources.getStringArray(resId)
}

fun Context.boolean(@BoolRes bool: Int): Boolean {
    return resources.getBoolean(bool)
}

fun Fragment.boolean(@BoolRes bool: Int): Boolean {
    return requireContext().boolean(bool)
}

fun View.boolean(@BoolRes bool: Int): Boolean {
    return context.boolean(bool)
}

fun Context.int(@IntegerRes int: Int): Int {
    return resources.getInteger(int)
}

fun Fragment.int(@IntegerRes int: Int): Int {
    return requireContext().resources.getInteger(int)
}

fun View.int(@IntegerRes int: Int): Int {
    return context.resources.getInteger(int)
}