package com.example.fitfit.data

import com.google.gson.annotations.SerializedName

class User(@SerializedName("id") val id: String,
       @SerializedName("password") val password: String,
       @SerializedName("loginType") val loginType: String,
       @SerializedName("nickname")  val nickname: String,
       @SerializedName("profileImagePath") val profileImagePath: String,
       @SerializedName("subscribtion") val subscribtion: String,
       @SerializedName("result") val result: String?) {
}