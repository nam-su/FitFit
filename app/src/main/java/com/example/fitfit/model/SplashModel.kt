package com.example.fitfit.model

import android.content.Context
import android.content.SharedPreferences
import com.example.fitfit.`class`.MyApplication
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface

class SplashModel() {

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    // 로그인 정보 확인하는 메서드
    fun checkLogin(): Boolean {

        return when(MyApplication.sharedPreferences.getUserId()) {

            ""-> false
            else -> true

        }

    } // checkLogin()

}