package com.example.fitfit.model

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.fitfit.data.User
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Call
import retrofit2.Response

class LoginModel(context: Context) {

    private val TAG = "로그인 모델"

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    var sharedPreferences: SharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()


    // 로그인 통신으로 result 값 확인
    suspend fun login(id: String, password: String,mode: String): User? {

        val response = retrofitInterface.selectUserData(id,password,mode)

        return response.body()

    } // login()


    // 로그인 성공시 쉐어드에 유저정보 저장.
    fun setSharedPreferencesUserInfo(user: User) {

        editor.putString("id",user.id)
        editor.putString("nickname",user.nickname)
        editor.putString("loginType",user.loginType)
        editor.putString("profileImagePath",user.profileImagePath)
        editor.putString("subscribtion",user.subscribtion)
        editor.commit()

    } // setSharedPreferencesUserInfo()

}