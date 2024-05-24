package com.example.fitfit.`class`

import android.content.Context
import com.example.fitfit.data.User

class Preferences(context: Context) {

    private val preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE)
    private val editor = preferences.edit()


    // 유저 정보 불러오는 메서드
    fun getUser(): User {

        preferences.getString("id", "")
        preferences.getString("nickname", "")
        preferences.getString("loginType", "")
        preferences.getString("profileImagePath", "")
        preferences.getString("subscribtion", "")

        return User(
            preferences.getString("id", "").toString(),
            preferences.getString("loginType", "").toString(),
            preferences.getString("nickname", "").toString(),
            preferences.getString("profileImagePath", "").toString(),
            preferences.getString("subscribtion", "").toString()
        )

    } // getUser()


    // 유저 아이디만 조회하는 메서드
    fun getUserId(): String {

        return preferences.getString("id", "").toString()

    } // getUserId()


    // 유저 닉네임만 조회하는 메서드
    fun getUserNickname(): String {

        return preferences.getString("nickname", "").toString()

    } // getUserNickname()


    // 유저 정보 쉐어드에 저장 하는 메서드
    fun setUser(user: User) {

        editor.putString("id",user.id)
        editor.putString("nickname",user.nickname)
        editor.putString("loginType",user.loginType)
        editor.putString("profileImagePath",user.profileImagePath)
        editor.putString("subscribtion",user.subscribtion)
        editor.apply()

    } // setUser()

}