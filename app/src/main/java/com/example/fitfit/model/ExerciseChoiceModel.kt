package com.example.fitfit.model

import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import java.time.LocalDate

class ExerciseChoiceModel {

    // 리사이클러뷰에 들어가는 어레이리스트 초기화 메서드
    fun setExerciseChoiceList(): ArrayList<PoseExercise> {

        val exerciseChoiceList = MyApplication.sharedPreferences.getMyPoseExerciseList()

        when(exerciseChoiceList.size) {

            0 ->{

                exerciseChoiceList.add(PoseExercise(0,"스쿼트","기본 스쿼트",0,10,1))
                exerciseChoiceList.add(PoseExercise(0,"푸시업","기본 푸시업",0,10,1))
                exerciseChoiceList.add(PoseExercise(0,"런지","기본 런지",0,10,1))

                MyApplication.sharedPreferences.setMyPoseExerciseList(exerciseChoiceList)

            }

            else -> {

                compareExerciseDate(exerciseChoiceList)

            }

        }

        return exerciseChoiceList

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