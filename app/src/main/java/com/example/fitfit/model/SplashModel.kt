package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.SplashResponse
import com.example.fitfit.data.User
import com.example.fitfit.function.MyApplication
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Response

class SplashModel {

    private val TAG = "스플래시 모델"

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
    suspend fun selectUserExercise(): Response<SplashResponse> {

        val id = MyApplication.sharedPreferences.getUserId()
        val mode = "selectUserExerciseFromId"

        return retrofitInterface.selectUserExercise(id,mode)

    } // selectUserExercise()


    // 서버에서 불러온 유저 운동정보 쉐어드에 저장하는 메서드
    fun saveUserExerciseInfo(userAllExerciseList: ArrayList<PoseExercise>) {

        MyApplication.sharedPreferences.setUserExerciseInfoList(userAllExerciseList)
        Log.d(TAG, "${MyApplication.sharedPreferences} ")
        Log.d(TAG, "saveUserExerciseInfo: ${MyApplication.sharedPreferences.getMyAllExerciseList()}")

    } // saveUserExerciseInfo()


    //서버에서 불러온 유저 체크리스트로 운동편집리스트 갱신메서드
    fun saveUserCheckList(userCheckListHashMap: HashMap<String,Int>){

        MyApplication.sharedPreferences.setAllExerciseList(userCheckListHashMap)

    } // saveUserCheckList()

}