package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.`class`.GmailSender
import com.example.fitfit.data.User
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface

class SignUpModel {

    private val TAG = "회원가입 모델"

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    lateinit var id:String

    // 로그인 통신으로 result 값 확인
    suspend fun duplicateCheckId(id: String, password: String,mode: String): User? {

        val response = retrofitInterface.selectUserData(id,password,mode)
        return response.body()

    } // login()


    fun sendMail(email:String){
        GmailSender().sendEmail(email)
        Log.d(TAG, "sendMail: 메일전송")
    }

}