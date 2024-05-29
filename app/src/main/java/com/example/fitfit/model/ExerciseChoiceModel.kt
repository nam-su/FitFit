package com.example.fitfit.model

import com.example.fitfit.data.ExerciseChoice
import com.example.fitfit.data.ExerciseDiary

class ExerciseChoiceModel {


    fun setExerciseChoiceList(): ArrayList<ExerciseChoice> {

        val exerciseChoiceList = ArrayList<ExerciseChoice>()

        exerciseChoiceList.add(ExerciseChoice("스쿼트","기본 스쿼트"))
        exerciseChoiceList.add(ExerciseChoice("푸시업","기본 푸시업"))
        exerciseChoiceList.add(ExerciseChoice("런지","기본 런지"))

        return exerciseChoiceList
    }

}