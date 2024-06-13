package com.example.fitfit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.model.ExerciseEditModel

class ExerciseEditViewModel: ViewModel() {

    private val exerciseEditModel = ExerciseEditModel()

    private val _checkMyExerciseListSizeMax = MutableLiveData<Boolean>()
    val checkMyExerciseListSizeMax: LiveData<Boolean>
        get() = _checkMyExerciseListSizeMax

    private val _myExerciseListSize = MutableLiveData<Int>()
    val myExerciseListSize: LiveData<Int>
        get() = _myExerciseListSize


    // 내 운동 리스트
    fun getSharedMyExerciseList(): ArrayList<PoseExercise> {

        _myExerciseListSize.value = exerciseEditModel.myExerciseList.size

        return exerciseEditModel.getSharedMyExerciseList()

    } // setMyExerciseList()


    // 모든 스쿼트 리스트
    fun setAllSquatList(): ArrayList<PoseExercise> {

        return exerciseEditModel.getAllSquatList()

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
        _myExerciseListSize.value = exerciseEditModel.myExerciseList.size

    } // deleteExerciseItem()


    // 리스트에 아이템 추가
    fun addExerciseItem(poseExerciseList: ArrayList<PoseExercise>,position: Int) {

        _checkMyExerciseListSizeMax.value = exerciseEditModel.addExerciseItem(poseExerciseList[position])

        _myExerciseListSize.value = exerciseEditModel.myExerciseList.size

    } // addExerciseItem()


    // 내 운동 리스트가 최소 3개인지 판별
    fun checkMyExerciseListSizeMin(): Boolean{

        return exerciseEditModel.myExerciseList.size > 2

    } // checkMyExerciseListSizeMin()

}