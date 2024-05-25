package com.example.fitfit.model

import com.example.fitfit.`class`.MyApplication
import com.example.fitfit.data.User

class UserModel {

    fun getUser(): User {

        return MyApplication.sharedPreferences.getUser()

    }

}