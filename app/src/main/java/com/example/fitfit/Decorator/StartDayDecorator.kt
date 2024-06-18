package com.example.fitfit.Decorator

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.Date

class StartDayDecorator(private val startDate: Date): DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {

        return day.date.before(startDate)
    }

    override fun decorate(view: DayViewFacade) {

        // 날짜를 장식하는 방법 설정
        view.setDaysDisabled(true) // 날짜 비활성화
        view.addSpan(ForegroundColorSpan(Color.GRAY)) // 텍스트 색상 변경
    }
}