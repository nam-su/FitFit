package com.example.fitfit.model

import android.content.Context
import android.content.SharedPreferences
import com.example.fitfit.`class`.MyApplication

class HomeModel() {


    // 홈에서 이번주 운동 상태 관련 메시지 정보
    fun setWeekStatus(): String {

        return MyApplication.sharedPreferences.getUserNickname()

    } // setWeekStatus()

}