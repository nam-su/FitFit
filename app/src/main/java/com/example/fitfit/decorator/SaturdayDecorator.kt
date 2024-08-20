package com.example.fitfit.decorator

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.Calendar

class SaturdayDecorator : DayViewDecorator {

    private val calendar = Calendar.getInstance()

    // shouldDecorate 메서드
    override fun shouldDecorate(day: CalendarDay): Boolean {

        day.copyTo(calendar)

        val weekDay = calendar[Calendar.DAY_OF_WEEK]

        return weekDay == Calendar.SATURDAY

    } // shouldDecorate()


    // decorate 메서드
    override fun decorate(view: DayViewFacade) = view.addSpan(ForegroundColorSpan(Color.BLUE))
    // decorate()

}
