package com.chenyue404.androidlib.util

import android.os.Build
import android.text.Html
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * 编解码工具
 */
object EncodeUtil {
    /**
     * url编码
     * @param input 待处理字符串
     * @param charsetName 编码方案，默认UTF-8
     */
    fun urlEncode(input: String, charsetName: String = "UTF-8"): String {
        return if (input.isEmpty()) {
            ""
        } else {
            URLEncoder.encode(input, charsetName)
        }
    }

    /**
     * url解码
     * @param input 待处理字符串
     * @param charsetName 编码方案，默认UTF-8
     */
    fun urlDecode(input: String, charsetName: String = "UTF-8"): String {
        return if (input.isEmpty()) {
            ""
        } else {
            URLDecoder.decode(
                input.replace("%(?![0-9a-fA-F]{2})", "%25")
                    .replace("\\+", "%2B"),
                charsetName
            )
        }
    }

    /**
     * html编码
     * @param input 待处理字符串
     */
    fun htmlEncode(input: CharSequence): String {
        if (input.isEmpty()) return ""
        val sb = StringBuilder()
        input.forEach {
            when (it) {
                '<' -> sb.append("&lt;")
                '>' -> sb.append("&gt;")
                '&' -> sb.append("&amp;")
                '\'' -> sb.append("&#39;")
                '"' -> sb.append("&quot;")
                else -> sb.append(it)
            }
        }
        return sb.toString()
    }

    /**
     * html解码
     * @param input 待处理字符串
     */
    fun htmlDecode(input: String): CharSequence {
        return when {
            input.isEmpty() -> ""
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(
                input,
                Html.FROM_HTML_MODE_LEGACY
            )
            else -> Html.fromHtml(input)
        }
    }
}