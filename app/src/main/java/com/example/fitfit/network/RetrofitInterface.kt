package com.example.fitfit.network

import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
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


    // 운동 정보를 insert 해주는 메서드
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun insertPoseExercise(

        @Field("id") id: String,
        @Field("category") category: String,
        @Field("exerciseName") exerciseName: String,
        @Field("exerciseCount") exerciseCount: Int,
        @Field("goalExerciseCount") goalExerciseCount: Int,
        @Field("date") date: Long,
        @Field("mode") mode: String

    ): Response<PoseExercise>


    // 운동 정보 update 해주는 메서드
    @FormUrlEncoded
    @POST("userProcess.php")
    suspend fun updatePoseExercise(

        @Field("id") id: String,
        @Field("category") category: String,
        @Field("exerciseName") exerciseName: String,
        @Field("exerciseCount") exerciseCount: Int,
        @Field("goalExerciseCount") goalExerciseCount: Int,
        @Field("date") date: Long,
        @Field("mode") mode: String

    ): Response<PoseExercise>

}