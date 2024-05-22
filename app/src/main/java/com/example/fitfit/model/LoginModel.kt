package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.User
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface

class LoginModel {

    private val TAG = "로그인 모델"

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)


    // 로그인 통신으로 result 값 확인
    suspend fun login(id: String, password: String,mode: String): User? {

        val response = retrofitInterface.selectUserData(id,password,mode)
        return response.body()

    } // login()

}