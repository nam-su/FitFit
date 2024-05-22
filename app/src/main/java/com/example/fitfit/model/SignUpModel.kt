package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.`class`.GmailSender

class SignUpModel {

    private val TAG = "회원가입 모델"

    fun sendMail(email:String){
        GmailSender().sendEmail(email)
        Log.d(TAG, "sendMail: 메일전송")
    }

}