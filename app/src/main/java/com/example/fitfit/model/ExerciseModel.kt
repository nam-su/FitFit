package com.example.fitfit.model

import com.example.fitfit.data.Challenge
import com.example.fitfit.data.ChallengeResponse
import com.example.fitfit.data.ExerciseInfo
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Response

class ExerciseModel {

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(
        RetrofitInterface::class.java)



    // 쉐어드에 있는 내 운동리스트 가져오는 메서드
    fun getMyExerciseList(): ArrayList<PoseExercise> {

        return MyApplication.sharedPreferences.getMyPoseExerciseList()

    } // getMyExerciseList()


    // 운동정보 상세보기 리스트 가져오는 메서드
    fun getMyExerciseInfoList(): ArrayList<ExerciseInfo> {

        val exerciseDetailViewList = ArrayList<ExerciseInfo>()

        exerciseDetailViewList.add(ExerciseInfo("하체","기본 스쿼트",50,10))
        exerciseDetailViewList.add(ExerciseInfo("상체","기본 푸시업",150,10))
        exerciseDetailViewList.add(ExerciseInfo("하체","기본 런지",3350,10))

        return exerciseDetailViewList

    }


    // 싱글톤의 챌린지 리스트 호출
    fun getChallengeList(): ArrayList<Challenge> = MyApplication.sharedPreferences.challengeList
    //getChallengeList()


    // 레트로핏에서 baseurl 경로 받아오기
    fun getBaseUrl(): String = retrofitBuilder.baseUrl.toString()
    //getBaseUrl()


    //챌린지 참여 서버 등록
    suspend fun challengeJoin(challenge: Challenge?): Response<ChallengeResponse>?
    = retrofitInterface.challengeJoin(MyApplication.sharedPreferences.getUserId(),challenge?.challengeName,"challengeJoin")
    // challengeJoin


    //서버에서 받아온 챌린지 리스트를 싱글톤에 저장하는 메서드
    fun saveChallengeList(challengeList: ArrayList<Challenge>) {

        MyApplication.sharedPreferences.challengeList.clear()
        MyApplication.sharedPreferences.challengeList.addAll(challengeList)

    } //saveChallengeList()


    // 서버에서 받아온 내 챌린지 리스트를 싱글톤에 저장하는 메서드
//    fun saveMyChallengeList(myChallengeList: ArrayList<Challenge>) {
//
//        MyApplication.sharedPreferences.myChallengeList.clear()
//        MyApplication.sharedPreferences.myChallengeList.addAll(myChallengeList)
//
//    }


}