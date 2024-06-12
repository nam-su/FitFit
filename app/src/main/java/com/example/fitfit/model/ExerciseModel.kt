package com.example.fitfit.model

import com.example.fitfit.data.ExerciseInfo
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication

class ExerciseModel {

    // 쉐어드에 있는 내 운동리스트 가져오는 메서드
    fun getMyExerciseList(): ArrayList<PoseExercise> {

        return MyApplication.sharedPreferences.getMyPoseExerciseList()

    } // getMyExerciseList()


    // 운동정보 상세보기 리스트 가져오는 메서드
    fun getMyExerciseInfoList(): ArrayList<ExerciseInfo> {

        val exerciseDetailViewList = ArrayList<ExerciseInfo>()

        exerciseDetailViewList.add(ExerciseInfo("하체","기본 스쿼트",50,10))
        exerciseDetailViewList.add(ExerciseInfo("상체","기본 푸시업",150,10))
        exerciseDetailViewList.add(ExerciseInfo("하체","기본 런지",3350,10))

        return exerciseDetailViewList

    }


}