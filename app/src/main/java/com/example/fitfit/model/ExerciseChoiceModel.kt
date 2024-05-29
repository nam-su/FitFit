package com.example.fitfit.model

import com.example.fitfit.data.ExerciseChoice
import com.example.fitfit.data.ExerciseDiary

class ExerciseChoiceModel {

    // 리사이클러뷰에 들어가는 어레이리스트 초기화 메서드
    fun setExerciseChoiceList(): ArrayList<ExerciseChoice> {

        val exerciseChoiceList = ArrayList<ExerciseChoice>()

        // 여기서 exerciseCount 는 쉐어드에서 호출해서 지정해준다.
        exerciseChoiceList.add(ExerciseChoice("스쿼트","기본 스쿼트",0,10))
        exerciseChoiceList.add(ExerciseChoice("푸시업","기본 푸시업",0,10))
        exerciseChoiceList.add(ExerciseChoice("런지","기본 런지",0,10))

        return exerciseChoiceList

    } // setExerciseChoiceList()

}