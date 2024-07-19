package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import java.time.LocalDate

class ExerciseChoiceModel {

    private val TAG = "운동 선택 모델"

    // 리사이클러뷰에 들어가는 어레이리스트 초기화 메서드
    fun setExerciseChoiceList(): ArrayList<PoseExercise> {

        MyApplication.sharedPreferences.getMyPoseExerciseList().forEach {

            Log.d(TAG, "setExerciseChoiceList: ${it.exerciseName}")

        }

        return compareExerciseDate(MyApplication.sharedPreferences.getMyPoseExerciseList())

    } // setExerciseChoiceList()


    // 최근 운동한 날짜가 오늘과 다르다면 운동 카운트 0으로 초기화 하는 메서드
    private fun compareExerciseDate(myExerciseList: ArrayList<PoseExercise>): ArrayList<PoseExercise> {

        val todayDate = LocalDate.now()

        // 내 운동리스트 순회
        for (exercise in myExerciseList) {

            // 최근 운동의 운동한 날짜.
            val exerciseDate = LocalDate.ofEpochDay(exercise.date / (24 * 60 * 60 * 1000))

            // 오늘날짜와 마지막 운동 기록의 날짜가 일치하지 않을 때
            if(todayDate != exerciseDate) {

                exercise.date = System.currentTimeMillis()
                exercise.exerciseCount = 0

            }

        }

        return myExerciseList

    } // compareExerciseDate()

}