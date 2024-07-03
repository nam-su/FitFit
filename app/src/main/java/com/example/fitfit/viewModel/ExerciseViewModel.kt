package com.example.fitfit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.ExerciseInfo
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.model.ExerciseModel
import java.util.Date

class ExerciseViewModel: ViewModel() {

    private val exerciseModel = ExerciseModel()

    private var _challenge = MutableLiveData<Challenge>()
    val challenge: LiveData<Challenge>
        get() = _challenge

    // 내 운동 리스트 받아오는 메서드
    fun getMyExerciseList(): ArrayList<PoseExercise> {

        return exerciseModel.getMyExerciseList()

    } // getMyExerciseList()


    // 내 운동 리스트 운동 자세히보기 리스트
    fun getMyExerciseInfoList(): ArrayList<ExerciseInfo> {

        return exerciseModel.getMyExerciseInfoList()

    } // getMyExerciseInfoList()


    // fitfit 챌린지 리스트 모델에서 호출
    fun getChallengeList(): ArrayList<Challenge> = exerciseModel.getChallengeList()
    // getChallengeList()

    //baseURL 받아오기
    fun getBaseUrl(): String = exerciseModel.getBaseUrl()
    // getBaseUrl()




}