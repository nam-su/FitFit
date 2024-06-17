package com.example.fitfit.Decorator

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.fitfit.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.Date

class TodayDecorator(private val context: Context) : DayViewDecorator {
    private var date: CalendarDay

    init {
        date = CalendarDay.today()
        Log.d("오늘날짜", "TodayDecorator: $date")
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == date
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_circle)!!)
    }

    fun setDate(date: Date?) {
        this.date = CalendarDay.from(date)
    }
}
