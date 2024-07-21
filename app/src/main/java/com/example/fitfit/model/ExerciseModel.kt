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

    private val exerciseInfoList = ArrayList<ExerciseInfo>()
    private lateinit var myExerciseList: ArrayList<PoseExercise>


    // 운동 정보 리스트에 운동 객체 추가
    init {

        exerciseInfoList.add(ExerciseInfo("하체","기본 스쿼트",40,10))
        exerciseInfoList.add(ExerciseInfo("상체","기본 푸시업",20,10))
        exerciseInfoList.add(ExerciseInfo("하체","기본 런지",80,10))
        exerciseInfoList.add(ExerciseInfo("하체","와이드 스쿼트",50,10))
        exerciseInfoList.add(ExerciseInfo("하체","기본 레그레이즈",60,10))
        exerciseInfoList.add(ExerciseInfo("하체","왼쪽 런지",70,10))
        exerciseInfoList.add(ExerciseInfo("하체","오른쪽 런지",70,10))
        exerciseInfoList.add(ExerciseInfo("하체","왼쪽 레그레이즈",50,10))
        exerciseInfoList.add(ExerciseInfo("하체","오른쪽 레그레이즈",50,10))

    }


    // 쉐어드에 있는 내 운동리스트 가져오는 메서드
    fun getMyExerciseList(): ArrayList<PoseExercise> {

        myExerciseList = MyApplication.sharedPreferences.getMyPoseExerciseList()

        return myExerciseList

    } // getMyExerciseList()


    // 운동정보 상세보기 리스트 가져오는 메서드
    fun getMyExerciseInfoList(): ArrayList<ExerciseInfo> {

        val exerciseDetailViewList = ArrayList<ExerciseInfo>()

        // myExerciseList의 이름들을 Set에 저장
        val myExerciseNames = myExerciseList.map { it.exerciseName }.toSet()

        // exerciseInfoList를 순회하면서 이름이 겹치는 항목을 찾기
        for (exerciseInfo in exerciseInfoList) {

            if (exerciseInfo.exerciseName in myExerciseNames) {

                exerciseDetailViewList.add(exerciseInfo)

            }

        }

        return exerciseDetailViewList

    } // getMyExerciseInfoList()


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

}