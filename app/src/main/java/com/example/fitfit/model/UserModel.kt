package com.example.fitfit.model

import com.example.fitfit.function.MyApplication
import com.example.fitfit.data.User
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Response

class UserModel {

    private val TAG = "유저 모델"



    var id = ""
    var password = ""
    var nickname = ""
    var randomString = ""


    // 회원 탈퇴 통신 으로 result 값 확인
    suspend fun withdrawalProcess(id: String, mode: String): Response<User> {

        val retrofitBuilder = RetrofitBuilder()
        val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

        return retrofitInterface.withdrawal(id,mode)

    } // withdrawalProcess()


    // 유저정보 쉐어드에서 가져옴
    fun getUser(): User = MyApplication.sharedPreferences.getUser()
    // getUser()


    // 쉐어드에 유저 정보 삭제
    fun setSharedPreferencesRemoveUserInfo() {

        MyApplication.sharedPreferences.removeAll()

    } // setSharedPreferencesUserInfo()


    // 구독권 만료 서버에 데이터 삭제
    suspend fun deleteSubscription(): Response<User> {

        val retrofitBuilder = RetrofitBuilder()
        val retrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

        return retrofitInterface.deleteSubscription(getUser().id)

    } // updateSubscription()


    //유저 정보 저장하기
    fun setUser(user: User) = MyApplication.sharedPreferences.setUser(user)

}