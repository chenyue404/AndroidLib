package com.chenyue404.androidlib.extends

import android.content.res.Resources
import android.util.TypedValue

/** int dp to Px */
fun Int.dp2Px(): Int =
    (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

/** float dp to Px */
fun Float.dp2Px(): Int = (TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    Resources.getSystem().displayMetrics
) + 0.5f).toInt()

/** int px to dp */
fun Int.px2dp(): Int = (this / Resources.getSystem().displayMetrics.density + 0.5f).toInt()

/** float px to dp */
fun Float.px2dp(): Int = (this / Resources.getSystem().displayMetrics.density + 0.5f).toInt()

/** int sp to Px*/
fun Int.sp2Px(): Int = (this * Resources.getSystem().displayMetrics.scaledDensity + 0.5f).toInt()

/** float sp to Px */
fun Float.sp2Px(): Int = (this * Resources.getSystem().displayMetrics.scaledDensity + 0.5f).toInt()