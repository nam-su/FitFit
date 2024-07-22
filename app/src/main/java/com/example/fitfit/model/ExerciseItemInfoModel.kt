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


    // 유저 구독 유무 체크 메서드
    fun checkUserSubscribe():Boolean {

        var checkUserSubscribe = false

        if(MyApplication.sharedPreferences.getUser().subscription != "") {

            checkUserSubscribe = true

        }

        return checkUserSubscribe

    } // checkUserSubscribe()

}