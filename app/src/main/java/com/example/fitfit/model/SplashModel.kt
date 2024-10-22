package com.example.fitfit.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.SplashResponse
import com.example.fitfit.data.User
import com.example.fitfit.function.MyApplication
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Response

class SplashModel {

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
    fun saveUserExerciseInfo(userAllExerciseList: ArrayList<PoseExercise>) =
        MyApplication.sharedPreferences.setUserExerciseInfoList(userAllExerciseList)
    // saveUserExerciseInfo()


    //서버에서 불러온 유저 체크리스트 정보를 토대로 싱글톤 전체 리스트를 만드는 메서드 호출
    fun saveUserCheckList(checkList: String) = MyApplication.sharedPreferences.setAllExerciseList(checkList)
    // saveUserCheckList()


    //서버에서 받아온 챌린지 리스트를 싱글톤에 저장하는 메서드
    fun saveChallengeList(challengeList: ArrayList<Challenge>){

        MyApplication.sharedPreferences.challengeList.clear()
        MyApplication.sharedPreferences.challengeList.addAll(challengeList)

    } // saveChallengeList()


    // 네트워크 연결 상태를 논리 값으로 반환
    // 와이파이, 모바일 데이터 연결 중일 경우 true 반환
    fun getNetworkStatus(context: Context): Boolean {

        var networkStatus = false

        val connectivityManager : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network : Network? = connectivityManager.activeNetwork
        val actNetwork : NetworkCapabilities? = connectivityManager.getNetworkCapabilities(network)

        if (actNetwork == null) {

            networkStatus = false

        } else if (

            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

            networkStatus = true

        }

        return networkStatus

    } // getNetworkStatus()

}