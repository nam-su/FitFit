package com.example.fitfit.model

import com.example.fitfit.data.ExerciseItemInfo
import com.example.fitfit.function.MyApplication

class ExerciseItemInfoModel {

    lateinit var exerciseItemInfo: ExerciseItemInfo

    var exerciseIndex = 0


    // 운동 정보 초기화 메서드
    fun setExerciseItemInfo(exerciseName: String) {

        exerciseItemInfo = MyApplication.sharedPreferences.getExerciseItemInfo(exerciseName)!!

    } // setExerciseItemInfo()


}