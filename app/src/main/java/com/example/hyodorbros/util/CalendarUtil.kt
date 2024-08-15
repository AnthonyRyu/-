package com.example.hyodorbros.util

import android.content.Context
import com.example.hyodorbros.R
import com.example.hyodorbros.constant.Constant
import java.util.*
import kotlin.math.abs

object CalendarUtil {
    /**
     * 두 날짜 간 차이 구하기
     * @param context
     * @param mTargetCalendar
     * @param mBaseCalendar
     * @param year
     * @param month
     * @param day
     * @return
     */
    fun getDiffDays(
        context: Context,
        targetCalendar: Calendar,
        baseCalendar: Calendar = GregorianCalendar(),
        direction: Int = Constant.DIRECTION.FORWARD
    ): String {

        // 밀리초(1000분의 1초) 단위로 두 날짜 간 차이를 변환 후 초 단위로 다시 변환
        val diffSec = (targetCalendar.timeInMillis - baseCalendar.timeInMillis) / 1000
        // 1분(60초), 1시간(60분), 1일(24시간) 이므로 다음과 같이 나누어 1일 단위로 다시 변환
        val diffDays = diffSec / (60 * 60 * 24)
        var flag = 0
        if (direction == Constant.DIRECTION.FORWARD) {
            flag = if (diffDays > 0) 1 else if (diffDays < 0) -1 else 0
        } else if (direction == Constant.DIRECTION.REVERSE) {
            flag = if (diffDays > 0) -1 else if (diffDays < 0) 1 else 0
        }
        val msg: String = when (flag) {
            1 -> context.getString(R.string.dday_valid_prefix) + abs(diffDays)
            0 -> context.getString(R.string.dday_today)
            -1 -> context.getString(R.string.dday_invalid_prefix) + abs(diffDays)
            else -> ""
        }
        return msg
    }
}