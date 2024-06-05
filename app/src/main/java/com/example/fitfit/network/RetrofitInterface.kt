package com.example.fitfit.network

import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.Objects

interface RetrofitInterface {

    // 유저 정보 불러오기 로그인,비밀번호 찾기
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun selectUserData(

        @Field("id") id: String,
        @Field("password") password: String,
        @Field("nickname") nickname: String,
        @Field("mode") mode: String

    ): Response<User>



    // 회원가입
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun signUp(

        @Field("id") id: String,
        @Field("password") password: String,
        @Field("nickname") nickname: String,
        @Field("mode") mode: String

    ): Response<User>



    // 회원 탈퇴
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun withdrawal(

        @Field("id") id: String,
        @Field("mode") mode: String

    ): Response<User>



    //운동정보 추가
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun insertIntoPoseExercise(

        @Field("id") id: String,
        @Field("category") category: String,
        @Field("exerciseName") exerciseName: String,
        @Field("exerciseCount") exerciseCount: Int,
        @Field("goalExerciseCount") goalExerciseCount: Int,
        @Field("date") date: String,
        @Field("mode") mode: String

    ): Response<PoseExercise>



    //프로필 수정
    @Multipart
    @POST("profileEditProcess.php")
    suspend fun profileEdit(

        @Part image: MultipartBody.Part?,
        @Part("id") id: RequestBody,
        @Part("nickname") nickname: RequestBody,
        @Part("mode") mode: RequestBody


        ): Response<User>



    //프로필 수정
    @FormUrlEncoded
    @POST("profileEditProcess.php")
    suspend fun profileEditWithoutImage(

        @Field("id") id: String,
        @Field("nickname") nickname: String,
        @Field("mode") mode: String

    ): Response<User>





}