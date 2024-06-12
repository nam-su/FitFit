package com.example.fitfit.viewModel

import androidx.lifecycle.ViewModel
import com.example.fitfit.data.ExerciseInfo
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.model.ExerciseModel

class ExerciseViewModel: ViewModel() {

    private val exerciseModel = ExerciseModel()

    // 내 운동 리스트 받아오는 메서드
    fun getMyExerciseList(): ArrayList<PoseExercise> {

        return exerciseModel.getMyExerciseList()

    } // getMyExerciseList()


    // 내 운동 리스트 운동 자세히보기 리스트
    fun getMyExerciseInfoList(): ArrayList<ExerciseInfo> {

        return exerciseModel.getMyExerciseInfoList()

    } // getMyExerciseInfoList()


}