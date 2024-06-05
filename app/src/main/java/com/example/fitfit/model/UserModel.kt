package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.function.MyApplication
import com.example.fitfit.data.User
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Response

class UserModel {

    private val TAG = "유저 모델"

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(
        RetrofitInterface::class.java)

    var id = ""
    var password = ""
    var nickname = ""
    var randomString = ""



    // 회원 탈퇴 통신 으로 result 값 확인
    suspend fun withdrawalProcess(id: String, mode: String) : Response<User> {

        return retrofitInterface.withdrawal(id,mode)

    } // withdrawalProcess()


    fun getUser(): User {

        return MyApplication.sharedPreferences.getUser()

    }



    // 쉐어드에 유저 정보 삭제
    fun setSharedPreferencesRemoveUserInfo() {

        MyApplication.sharedPreferences.removeAll()

    } // setSharedPreferencesUserInfo()



    // 레트로핏에서 baseurl 경로 받아오기
    fun getBaseUrl(): String{
        return retrofitBuilder.baseUrl.toString()
    }

}