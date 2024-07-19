package com.example.fitfit.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.ExerciseInfo
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.model.ExerciseModel
import kotlinx.coroutines.launch

class ExerciseViewModel: ViewModel() {

    private val TAG = "운동 뷰모델"
    private val exerciseModel = ExerciseModel()

    private var _joinResult = MutableLiveData<String>()
    val joinResult: LiveData<String>
        get() = _joinResult


    // 내 운동 리스트 받아오는 메서드
    fun getMyExerciseList(): ArrayList<PoseExercise> = exerciseModel.getMyExerciseList()
    // getMyExerciseList()


    // 내 운동 리스트 운동 자세히보기 리스트
    fun getMyExerciseInfoList(): ArrayList<ExerciseInfo> = exerciseModel.getMyExerciseInfoList()
    // getMyExerciseInfoList()


    // fitfit 챌린지 리스트 모델에서 호출
    fun getChallengeList(): ArrayList<Challenge> = exerciseModel.getChallengeList()
    // getChallengeList()


    //baseURL 받아오기
    fun getBaseUrl(): String = exerciseModel.getBaseUrl()
    // getBaseUrl()


    //모델에서 서버와 통신하는 메서드 호출
    fun challengeJoin(challenge: Challenge){

        viewModelScope.launch {

            val response = exerciseModel.challengeJoin(challenge)

            Log.d(TAG, "challengeJoin: ${response?.isSuccessful}")
            Log.d(TAG, "challengeJoin: ${response?.body()!!.result}")

            if(response.isSuccessful && response.body() != null){

               _joinResult.value = response.body()!!.result.toString()

                /** 운동 모델에서 싱글톤에 저장하는 메서드 호출**/
                if(response.body()?.challengeList != null) {

                    exerciseModel.saveChallengeList(response.body()?.challengeList!!)

                }

            }

        }

    } // challengeJoin()

}