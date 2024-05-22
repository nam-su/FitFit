package com.example.fitfit.model

import android.content.Context
import android.content.SharedPreferences

class HomeModel(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE)


    // 홈에서 이번주 운동 상태 관련 메시지 정보
    fun setWeekStatus(): String? {

        return sharedPreferences.getString("nickname","")

    } // setWeekStatus()

}