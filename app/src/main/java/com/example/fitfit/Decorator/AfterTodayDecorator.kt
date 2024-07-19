package com.example.fitfit.Decorator

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.Date

class AfterTodayDecorator: DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean = day.isAfter(CalendarDay.today())

    // decorate 메서드
    override fun decorate(view: DayViewFacade) {

        // 날짜를 장식하는 방법 설정
        view.setDaysDisabled(true) // 날짜 비활성화

        view.addSpan(ForegroundColorSpan(Color.TRANSPARENT)) // 텍스트 색상 변경

    } // decorate()

}