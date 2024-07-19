package com.example.fitfit.model

import com.example.fitfit.function.MyApplication
import com.example.fitfit.data.User
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Response

class UserModel {

    private val TAG = "유저 모델"

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface =
        retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    var id = ""
    var password = ""
    var nickname = ""
    var randomString = ""


    // 회원 탈퇴 통신 으로 result 값 확인
    suspend fun withdrawalProcess(id: String, mode: String): Response<User> = retrofitInterface.withdrawal(id,mode)
    // withdrawalProcess()


    // 유저정보 쉐어드에서 가져옴
    fun getUser(): User = MyApplication.sharedPreferences.getUser()
    // getUser()


    // 쉐어드에 유저 정보 삭제
    fun setSharedPreferencesRemoveUserInfo() {

        MyApplication.sharedPreferences.removeAll()

    } // setSharedPreferencesUserInfo()


    // 레트로핏에서 baseurl 경로 받아오기
    fun getBaseUrl(): String = retrofitBuilder.baseUrl.toString()
    // getBaseUrl()

}