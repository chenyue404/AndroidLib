package com.chenyue404.androidlib.util

import com.chenyue404.androidlib.util.DateUtil.ONE_DAY_TIME
import com.chenyue404.androidlib.util.DateUtil.ONE_HOUR_TIME
import com.chenyue404.androidlib.util.DateUtil.ONE_MIN_TIME
import com.chenyue404.androidlib.util.DateUtil.ONE_WEEK_TIME
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期和时间相关的工具
 * @property ONE_MIN_TIME 一分钟的毫秒数
 * @property ONE_HOUR_TIME 一小时的毫秒数
 * @property ONE_DAY_TIME 一天的毫秒数
 * @property ONE_WEEK_TIME 一星期的毫秒数
 * @author 王树正
 */
object DateUtil {
    const val ONE_MIN_TIME = 60 * 1000.toLong()
    const val ONE_HOUR_TIME = 60 * ONE_MIN_TIME
    const val ONE_DAY_TIME = 24 * ONE_HOUR_TIME
    const val ONE_WEEK_TIME = 7 * ONE_DAY_TIME

    //    字母 	描述 	示例
    //    G 	纪元标记 	AD
    //    y 	四位年份 	2001
    //    M 	月份 	July or 07
    //    d 	一个月的日期 	10
    //    h 	 A.M./P.M. (1~12)格式小时 	12
    //    H 	一天中的小时 (0~23) 	22
    //    m 	分钟数 	30
    //    s 	秒数 	55
    //    S 	毫秒数 	234
    //    E 	星期几 	Tuesday
    //    D 	一年中的日子 	360
    //    F 	一个月中第几周的周几 	2 (second Wed. in July)
    //    w 	一年中第几周 	40
    //    W 	一个月中第几周 	1
    //    a 	A.M./P.M. 标记 	PM
    //    k 	一天中的小时(1~24) 	24
    //    K 	 A.M./P.M. (0~11)格式小时 	10
    //    z 	时区 	Eastern Standard Time
    //    ' 	文字定界符 	Delimiter
    //    " 	单引号 	`
    const val FORMAT_DATE_WITH_TIME = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_DATE = "yyyy-MM-dd"

    /** 返回当前时间x年前的时间戳 */
    fun getTimeMillisBeforeYear(x: Int): Long {
        val c = Calendar.getInstance()
        c.add(Calendar.YEAR, -x)
        return c.time.time
    }

    /** 获取年龄 */
    fun getAge(year: Int, month: Int, day: Int): Int {
        var age: Int
        val cal = Calendar.getInstance()
        val yearNow = cal[Calendar.YEAR]
        val monthNow = cal[Calendar.MONTH] + 1
        val dayOfMonthNow = cal[Calendar.DAY_OF_MONTH]
        age = yearNow - year
        if (monthNow <= month) {
            if (monthNow == month) {
                if (dayOfMonthNow < day) {
                    age--
                }
            } else {
                age--
            }
        }
        return age
    }

    private fun Calendar.clear(vararg fields: Int) {
        for (f in fields) {
            this[f] = 0
        }
    }

    /** [timestamp]是不是今天 */
    fun isToday(timestamp: Long): Boolean {
        return isInXDays(timestamp, 0)
    }

    /** [timestamp]是不是昨天 */
    fun isYesterday(timestamp: Long): Boolean {
        return isInXDays(timestamp, 1)
    }

    /**
     * 秒数转换为时间
     * @param time 总秒数
     * @return 00:00:00
     */
    fun secToTime(time: Int): String {
        var timeStr = "00:00"
        val hour: Int
        var minute: Int
        val second: Int
        if (time > 0) {
            minute = time / 60
            if (minute < 60) {
                second = time % 60
                timeStr = unitFormat(minute) + ":" + unitFormat(second)
            } else {
                hour = minute / 60
                if (hour > 99) return "99:59:59"
                minute %= 60
                second = time - hour * 3600 - minute * 60
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
            }
        }
        return timeStr
    }

    private fun unitFormat(i: Int): String {
        return if (i in 0..9) "0$i" else "$i"
    }

    /**
     * 是否为x天内
     * @param time 时间戳，毫秒
     * @param xDays 天数
     */
    fun isInXDays(time: Long, xDays: Int): Boolean {
        val c = Calendar.getInstance().apply {
            clearDay()
        }
        val firstOfDay = c.timeInMillis // 今天最早时间
        c.apply {
            timeInMillis = time - ONE_DAY_TIME * xDays
            clearDay()
        }
        return firstOfDay == c.timeInMillis
    }

    /**
     * 时间戳转成String
     * @param time 时间戳，毫秒
     * @param pattern 日期格式，默认为[FORMAT_DATE_WITH_TIME]
     * @param locale 格式符号的区域
     */
    fun timeStampToString(
        time: Long,
        pattern: String = FORMAT_DATE_WITH_TIME,
        locale: Locale = Locale.ROOT
    ): String {
        return SimpleDateFormat(pattern, locale).format(Date(time))
    }

    /** 是不是下午 */
    fun isAfternoon(timestamp: Long): Boolean {
        return timestamp - Calendar.getInstance().apply {
            timeInMillis = timestamp
            clearDay()
        }.timeInMillis > ONE_HOUR_TIME * 12
    }

    /** 清除小时、分钟、秒、毫秒 */
    private fun Calendar.clearDay() = clear(
        Calendar.HOUR_OF_DAY,
        Calendar.MINUTE,
        Calendar.SECOND,
        Calendar.MILLISECOND
    )
}