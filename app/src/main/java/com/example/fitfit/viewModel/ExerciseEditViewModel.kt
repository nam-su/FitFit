package com.example.fitfit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.model.ExerciseEditModel

class ExerciseEditViewModel: ViewModel() {

    private val exerciseEditModel = ExerciseEditModel()


    // 내 운동 리스트
    fun setMyExerciseList(): ArrayList<PoseExercise> {

        return exerciseEditModel.setMyExerciseList()

    } // setMyExerciseList()


    // 모든 스쿼트 리스트
    fun setAllSquatList(): ArrayList<PoseExercise> {

        return exerciseEditModel.setAllSquatList()

    } // setAllSquatList()


    // 모든 푸시업 리스트
    fun setAllPushUpList(): ArrayList<PoseExercise> {

        return exerciseEditModel.getAllPushUpList()

    } // setAllPushUpList()


    // 모든 런지 리스트
    fun setAllLungeList(): ArrayList<PoseExercise> {

        return exerciseEditModel.getAllLungeList()

    } // setAllLungeList()


    // 리스트에서 아이템 삭제
    fun deleteExerciseItem(myExerciseList: ArrayList<PoseExercise>,position: Int) {

        exerciseEditModel.deleteExerciseItem(myExerciseList,position)

    } // deleteExerciseItem()

}