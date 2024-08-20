package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.function.GmailSender
import com.example.fitfit.data.User
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface

class SignUpModel {

    private val TAG = "회원가입 모델"

    var id = ""
    var password = ""
    var nickname = ""
    var randomString = ""

    /** 코드 만들어서 메일 보낸 시간 변수 **/
    var codeGeneratedTime : Long = 0

    /** 초 단위 **/
    var timeLimit = 180


    // 로그인 통신으로 result 값 확인
    suspend fun signUpProcess(id: String, password: String, nickname: String, mode: String): User {

        val retrofitBuilder = RetrofitBuilder()

        val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

        val response = retrofitInterface.signUp(id,password,nickname,mode)

        Log.d(TAG, "signUpProcess: ${response.isSuccessful}")

        return response.body()!!

    } // signUpProcess()


    // 이메일 발송 메서드
    fun sendMail(email:String){

        getRandomString()
        GmailSender().sendEmail(email,"FitFit 인증번호",randomString)

        codeGeneratedTime = System.currentTimeMillis()

    } // sendMail()


    //6자리 난수 만들기
    private fun getRandomString() {

            val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
            val randomString = (1..6)
                .map { charset.random() }
                .joinToString("")
            this.randomString = randomString

    } // getRandomString()

}