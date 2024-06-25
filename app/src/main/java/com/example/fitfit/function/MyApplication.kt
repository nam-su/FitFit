package com.example.fitfit.function

import android.app.Application
import android.util.Log

class MyApplication: Application() { // 쉐어드 싱글톤을 구현하기 위한 어플리케이션 클래스

    companion object{

        lateinit var sharedPreferences: Preferences

    }

    override fun onCreate() {

        super.onCreate()
        sharedPreferences = Preferences(applicationContext)
        
    }
    

}