package com.example.hyodorbros.ext

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.hyodorbros.util.CalendarUtil
import java.text.SimpleDateFormat
import java.util.*


fun Context.getDiffDays(date : String): String {
    val toIntDateList = date.split("/").map { it.toInt() }

    val calendar = GregorianCalendar().apply {
        set(toIntDateList[0], toIntDateList[1]-1, toIntDateList[2])
    }
    return CalendarUtil.getDiffDays(this, targetCalendar = calendar)
}

fun Fragment.getSelectDayString(year : Int, month : Int, date : Int) : String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, date)
    val sdf = SimpleDateFormat("yyyy/MM/dd")
    return sdf.format(calendar.timeInMillis).toString()
}

fun ViewModel.currentTimeString() : String {
    val sdf = SimpleDateFormat("yyyy/MM/dd")
    return sdf.format(System.currentTimeMillis()).toString()
}
