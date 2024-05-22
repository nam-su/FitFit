package com.example.fitfit.model

import android.content.Context
import android.content.SharedPreferences

class SplashModel(context: Context) {

    var sharedPreferences: SharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE)


    // 로그인 정보 확인하는 메서드
    fun checkLogin(): Boolean {

        return when(sharedPreferences.getString("id","")) {

            ""-> false
            else -> true

        }

    } // checkLogin()

}