package com.chenyue404.androidlib.extends

/**
 * 有条件取反。如果[condition]为true才取反
 */
fun Boolean.not(condition: Boolean) = if (condition) this.not() else this