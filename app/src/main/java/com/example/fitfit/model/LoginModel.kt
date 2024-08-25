package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import com.example.fitfit.data.User
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface

import retrofit2.Response

class LoginModel {

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    // 로그인 통신으로 result 값 확인
    suspend fun login(id: String, password: String, nickname: String,mode: String): Response<User> =
        retrofitInterface.selectUserData(id, password,nickname ,mode)
     // login()


    //소셜 로그인 통신으로 result 값 확인
    suspend fun socialLogin(id: String, loginType: String): Response<User> =
        retrofitInterface.socialLogin(id, loginType)
    // socialLogin()


    // 로그인 성공시 쉐어드에 유저정보 저장.
    fun setSharedPreferencesUserInfo(user: User): Boolean =
        MyApplication.sharedPreferences.setUserAndAllList(user)
    // setSharedPreferencesUserInfo()

}