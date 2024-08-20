package com.example.fitfit.decorator

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.fitfit.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayDecorator(private val context: Context) : DayViewDecorator {

    private var date: CalendarDay = CalendarDay.today()

    // shouldDecorate 메서드
    override fun shouldDecorate(day: CalendarDay): Boolean = day == date


    // decorate 메서드
    override fun decorate(view: DayViewFacade) =
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_circle)!!)
    // decorate()

}
