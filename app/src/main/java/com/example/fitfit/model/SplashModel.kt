package com.example.fitfit.model

import android.content.Context
import android.content.SharedPreferences
import com.example.fitfit.`class`.MyApplication

class SplashModel() {


    // 로그인 정보 확인하는 메서드
    fun checkLogin(): Boolean {

        return when(MyApplication.sharedPreferences.getUser().id) {

            ""-> false
            else -> true

        }

    } // checkLogin()

}