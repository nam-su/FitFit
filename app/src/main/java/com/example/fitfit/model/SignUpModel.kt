package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.`class`.GmailSender
import com.example.fitfit.data.User
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpModel {

    private val TAG = "회원가입 모델"

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    lateinit var id:String
    var randomString = ""

    // 로그인 통신으로 result 값 확인
    suspend fun duplicateCheckId(id: String, password: String,mode: String): User? {

        val response = retrofitInterface.selectUserData(id,password,mode)
        return response.body()

    } // login()


    fun sendMail(email:String){
        getRandomString()
        GmailSender().sendEmail(email,"FitFit 인증번호",randomString)
    }



    //6자리 난수 만들기
    fun getRandomString() {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        randomString = (1..6)
            .map { charset.random() }
            .joinToString("")
    }

}