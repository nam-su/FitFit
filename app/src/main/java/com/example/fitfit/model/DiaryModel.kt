package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication

class DiaryModel {

    private val TAG = "다이어리 모델"

    private val exerciseChoiceList = MyApplication.sharedPreferences.getMyPoseExerciseList()

    fun getMyPoseExerciseList(): ArrayList<PoseExercise> {
        return exerciseChoiceList
    }



}