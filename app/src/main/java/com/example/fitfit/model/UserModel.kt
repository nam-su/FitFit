package com.example.fitfit.model

import com.example.fitfit.function.MyApplication
import com.example.fitfit.data.User

class UserModel {

    fun getUser(): User {

        return MyApplication.sharedPreferences.getUser()

    }

}