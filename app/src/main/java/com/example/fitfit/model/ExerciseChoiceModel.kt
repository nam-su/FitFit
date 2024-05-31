package com.example.fitfit.model

import com.example.fitfit.data.ExerciseDiary
import com.example.fitfit.data.PoseExercise

class ExerciseChoiceModel {

    // 리사이클러뷰에 들어가는 어레이리스트 초기화 메서드
    fun setExerciseChoiceList(): ArrayList<PoseExercise> {

        val exerciseChoiceList = ArrayList<PoseExercise>()

        // 여기서 exerciseCount 는 쉐어드에서 호출해서 지정해준다.
        exerciseChoiceList.add(PoseExercise("2024-05-31","스쿼트","기본 스쿼트",0,10))
        exerciseChoiceList.add(PoseExercise("2024-05-31","푸시업","기본 푸시업",0,10))
        exerciseChoiceList.add(PoseExercise("2024-05-31","런지","기본 런지",0,10))

        return exerciseChoiceList

    } // setExerciseChoiceList()

}