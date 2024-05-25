package com.example.fitfit.data

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("id") val id: String,
       @SerializedName("loginType") val loginType: String,
       @SerializedName("nickname")  val nickname: String,
       @SerializedName("profileImagePath") val profileImagePath: String,
       @SerializedName("subscribtion") val subscribtion: String) {

       @SerializedName("result") var result: String? = null

}