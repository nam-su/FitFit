package com.example.fitfit.decorator

import android.content.Context
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.example.fitfit.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

//이번달에 속하지 않지만 캘린더에 보여지는 이전달/다음달의 일부 날짜를 설정하는 클래스
class SelectedMonthDecorator(var context: Context, var selectedMonth: Int): DayViewDecorator {

    // shouldDecorate 메서드
    override fun shouldDecorate(day: CalendarDay): Boolean = day.month != selectedMonth
    // shouldDecorate()


    // decorate 메서드
    override fun decorate(view: DayViewFacade) =
        view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.grey)))
    // decorate()

}