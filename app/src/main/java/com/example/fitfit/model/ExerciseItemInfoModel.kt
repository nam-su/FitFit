package com.example.fitfit.model

import com.example.fitfit.data.ExerciseItemInfo
import com.example.fitfit.function.MyApplication

class ExerciseItemInfoModel {

    lateinit var exerciseItemInfo: ExerciseItemInfo

    var exerciseIndex = 0

    fun setExerciseItemInfo(exerciseName: String) {

        exerciseItemInfo = MyApplication.sharedPreferences.getExerciseItemInfo(exerciseName)!!

    } // setExerciseItemInfo()


}