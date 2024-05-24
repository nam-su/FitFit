package com.example.fitfit.`class`

import android.app.Application

class MyApplication: Application() {

    companion object{
        lateinit var sharedPreferences: Preferences
    }

    override fun onCreate() {

        sharedPreferences = Preferences(applicationContext)
        super.onCreate()

    }

}