package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.function.GmailSender
import com.example.fitfit.data.User
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface

class FindPasswordModel {

    private val TAG = "비밀번호 찾기 모델"

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    var id = ""
    var password = ""
    var nickname = ""
    var randomString = ""

    /** 코드 만들어서 메일 보낸 시간 변수 **/
    var codeGeneratedTime : Long = 0
    var timeLimit = 180

    // 로그인 통신으로 result 값 확인
    suspend fun signUpProcess(id: String, password: String, nickname: String, mode: String): User? {

        val response = retrofitInterface.signUp(id,password,nickname,mode)
        Log.d(TAG, "signUpProcess: ${response.isSuccessful}")
        return response.body()

    } // login()


    fun sendMail(email:String){

        getRandomString()
        GmailSender().sendEmail(email,"FitFit 인증번호",randomString)

        codeGeneratedTime = System.currentTimeMillis()
    }



    //6자리 난수 만들기
    private fun getRandomString() {

        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        var randomString = (1..6)
            .map { charset.random() }
            .joinToString("")
        this.randomString = randomString
    }


}