package com.example.fitfit.function

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk

class MyApplication: Application() { // 쉐어드 싱글톤을 구현하기 위한 어플리케이션 클래스

    companion object{

        lateinit var sharedPreferences: Preferences

    }

    // onCreate
    override fun onCreate() {

        super.onCreate()

        KakaoSdk.init(this, "d997bc71e6bb7cad42042752aa3d4f9f")
        sharedPreferences = Preferences(applicationContext)

    } // onCreate()

}