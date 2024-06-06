package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication

class ExerciseEditModel {

    val TAG = "유저 운동 편집 모델"

    lateinit var myExerciseList: ArrayList<PoseExercise>

    private val allExerciseList = MyApplication.sharedPreferences.getAllExerciseList()

    private val allSquatList = ArrayList<PoseExercise>()

    private val allPushUpList = ArrayList<PoseExercise>()

    private val allLungeList = ArrayList<PoseExercise>()

    init {

        setAllCategoryList()

    }


    // 내 운동리스트 읽어와서 세팅한다.
    fun setMyExerciseList(): ArrayList<PoseExercise> {

        myExerciseList = MyApplication.sharedPreferences.getPoseExerciseList()

        return myExerciseList

    } // setMyExerciseList()


    // 전체 운동 리스트에서 카테고리별 리스트에 객체 추가
    private fun setAllCategoryList() {

        for (i:Int in 0 until allExerciseList.size) {

            when(allExerciseList[i].category){

                "스쿼트" -> allSquatList.add(allExerciseList[i])

                "푸시업" -> allPushUpList.add(allExerciseList[i])

                "런지" ->  allLungeList.add(allExerciseList[i])

            }

        }

    } // setAllCategoryList()


    // 모든 스쿼트 리스트 리턴
    fun getAllSquatList(): ArrayList<PoseExercise> {

        return allSquatList

    } // getAllSquatList()


    // 모든 푸시업 리스트 리턴하는 메서드
    fun getAllPushUpList(): ArrayList<PoseExercise> {

        return allPushUpList

    } // getAllPushUpList()


    // 모든 런지 리스트 리턴하는 메서드
    fun getAllLungeList(): ArrayList<PoseExercise> {

        return allLungeList

    } // getAllLungeList()


    // 내 운동 리스트에서 아이템 삭제하는 메서드
    fun deleteExerciseItem(myExerciseList: ArrayList<PoseExercise>,position: Int): Boolean {

        return if (myExerciseList.size > 3) {

            myExerciseList.removeAt(position)
            true

        } else {

            false

        }

    } // deleteExerciseItem()


    // 내 운동 리스트에 아이템 추가하는 메서드
    fun addExerciseItem(poseExercise: PoseExercise): Boolean {

        return if(myExerciseList.size < 20) {

            myExerciseList.add(poseExercise)
            true

        } else {

            false

        }

    } // addExerciseItem()

}