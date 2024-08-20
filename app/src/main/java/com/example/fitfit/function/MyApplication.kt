package com.example.fitfit.function

import android.app.Application
import com.example.fitfit.R
import com.kakao.sdk.common.KakaoSdk

class MyApplication: Application() { // 쉐어드 싱글톤을 구현하기 위한 어플리케이션 클래스

    companion object{

        lateinit var sharedPreferences: Preferences

    }

    // onCreate
    override fun onCreate() {

        super.onCreate()

        KakaoSdk.init(this, getString(R.string.kakaoAppKey))
        sharedPreferences = Preferences(applicationContext)

    } // onCreate()

}