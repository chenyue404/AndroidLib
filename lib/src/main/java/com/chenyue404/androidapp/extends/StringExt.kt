package com.chenyue404.androidapp.extends

/**
 * 返回一个list，包含当前字符串中的所有[findStr]的位置。
 * @param findStr 要找的字符串
 * @param ignoreCase 是否忽略大小写，默认false
 */
fun String.indexOfAll(findStr: String, ignoreCase: Boolean = false): ArrayList<Int> {
    val indexList = arrayListOf<Int>()
    var index = -2
    while (index != -1) {
        val startIndex = if (indexList.isEmpty()) 0 else (indexList.last() + 1)
        index = indexOf(findStr, startIndex, ignoreCase = ignoreCase)
        if (index != -1) {
            indexList.add(index)
        }
    }
    return indexList
}

/**
 * 判断是否为space
 */
fun CharSequence.isSpace(): Boolean {
    if (this.isEmpty()) {
        return true
    }
    this.forEach {
        if (!it.isWhitespace()) {
            return false
        }
    }
    return true
}