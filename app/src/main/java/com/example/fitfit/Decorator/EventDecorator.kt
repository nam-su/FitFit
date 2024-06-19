package com.example.fitfit.Decorator

import android.graphics.Color
import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EventDecorator(private val poseExerciseArrayList: ArrayList<PoseExercise>) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return getCalendarDayArrayList().contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(10f, Color.RED))
    }



    // 캘린더데이어레이리스트로 리턴하는 메서드
    private fun getCalendarDayArrayList(): ArrayList<CalendarDay>{

        val calendarDayArrayList = ArrayList<CalendarDay>()
        poseExerciseArrayList.forEach{ poseExercise ->
           calendarDayArrayList.add(dateToCalendarDay(poseExercise.date))
       }

        return calendarDayArrayList
    }



    //string 형태의 date를 calendarDay로 바꾸기
    private fun dateToCalendarDay(date: Long): CalendarDay{

        // 타임스탬프를 Instant로 변환
        val instant = Instant.ofEpochMilli(date)

        // Instant를 LocalDate로 변환
        val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()

        // LocalDate를 CalendarDay로 변환
        return CalendarDay.from(localDate.year, localDate.monthValue-1, localDate.dayOfMonth)


    } //dateToCalendarDay()
}