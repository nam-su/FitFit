package com.example.fitfit.network

import com.example.fitfit.data.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitInterface {

    // 유저 정보 불러오기 로그인,비밀번호 찾기
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun selectUserData(

        @Field("id") id: String,
        @Field("password") password: String,
        @Field("mode") mode: String

    ): Response<User>

}