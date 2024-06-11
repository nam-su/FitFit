package com.example.fitfit.model

import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Response

class SplashModel() {

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    // 로그인 정보 확인하는 메서드
    fun checkLogin(): Boolean {

        return when(MyApplication.sharedPreferences.getUserId()) {

            ""-> false
            else -> true

        }

    } // checkLogin()


    // 서버에 유저 아이디로 운동정보 요청하는 메서드
    suspend fun selectUserExercise(): Response<ArrayList<PoseExercise>> {

        val id = MyApplication.sharedPreferences.getUserId()
        val mode = "selectUserExerciseFromId"

        return retrofitInterface.selectUserExercise(id,mode)

    } // selectUserExercise()


    // 서버에서 불러온 유저 운동정보 쉐어드에 저장하는 메서드
    fun saveUserExerciseInfo(poseExerciseList: ArrayList<PoseExercise>) {

        MyApplication.sharedPreferences.setUserExerciseInfoList(poseExerciseList)

    }

}