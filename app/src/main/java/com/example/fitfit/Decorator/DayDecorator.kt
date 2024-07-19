package com.example.fitfit.Decorator

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.fitfit.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

//선택된 날짜의 Background를 설정하는 클래스
class DayDecorator(var context: Context) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean = true

    // decorate 메서드
    override fun decorate(view: DayViewFacade) {

        view.setSelectionDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_selector)!!)

    } // decorate()

}
