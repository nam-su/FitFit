package com.example.fitfit.model

import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import com.example.fitfit.`class`.MyApplication
import com.example.fitfit.data.ExerciseDiary

class HomeModel() {


    // 홈에서 이번주 운동 상태 관련 메시지 정보
    fun setWeekStatus(): String {

        return MyApplication.sharedPreferences.getUserNickname()

    } // setWeekStatus()


    // 유저 운동 정보 리스트 리턴하는 메서드
    fun setWeekStatusList(): ArrayList<ExerciseDiary>{

        val exerciseDiaryList = ArrayList<ExerciseDiary>()
        exerciseDiaryList.add(ExerciseDiary("일","true"))
        exerciseDiaryList.add(ExerciseDiary("월","true"))
        exerciseDiaryList.add(ExerciseDiary("화","true"))
        exerciseDiaryList.add(ExerciseDiary("수","false"))
        exerciseDiaryList.add(ExerciseDiary("목","false"))
        exerciseDiaryList.add(ExerciseDiary("금","false"))
        exerciseDiaryList.add(ExerciseDiary("토","false"))

        return exerciseDiaryList

    }

}