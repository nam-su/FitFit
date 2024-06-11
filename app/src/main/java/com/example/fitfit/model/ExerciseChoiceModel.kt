package com.example.fitfit.model

import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication

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

            }

        }

        return exerciseChoiceList

    } // setExerciseChoiceList()


    // 쉐어드에 운동정보 객체 저장
    fun setPoseExercise(poseExercise: PoseExercise) {

        MyApplication.sharedPreferences.setPoseExercise(poseExercise)

    } // savePoseExercse()

}