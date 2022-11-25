package com.chenyue404.androidlib.util

object BoolUtil {
    /**
     * 根据传入的bool数组返回对应的二进制字符串
     *
     * 传入true,false,false,true，返回"1001"
     */
    fun getBoolString(vararg boolArray: Boolean): String = getBoolBinary(*boolArray).toString(2)

    /**
     * 根据传入的bool数组返回对应的二进制Int
     *
     * 传入true,false,false,true，返回0b1001,9
     */
    fun getBoolBinary(vararg boolArray: Boolean): Int {
        var result = 0
        boolArray.forEach {
            result = result shl 1
            if (it) {
                result += 1
            }
        }
        return result
    }
}