package com.example.fitfit.viewModel

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.data.ExerciseItemInfo
import com.example.fitfit.model.ExerciseItemInfoModel


class ExerciseItemInfoViewModel(val exerciseName: String): ViewModel() {

    val TAG = "운동아이템정보 뷰모델"

    private val exerciseItemInfoModel = ExerciseItemInfoModel()

    // 인덱스 변수
    private val _exerciseItemIndex = MutableLiveData<Int>()
    val exerciseItemIndex: LiveData<Int> get() = _exerciseItemIndex

    // 운동 정보 내용
    private val _exerciseItemInfoContent = MutableLiveData<String>()
    val exerciseItemInfoContent: LiveData<String> get() = _exerciseItemInfoContent

    // 구독 유무 판단
    private val _isExerciseItemInfoPrimium = MutableLiveData<Boolean>()
    val isExerciseItemInfoPrimium: LiveData<Boolean> get() = _isExerciseItemInfoPrimium

    init {

        exerciseItemInfoModel.setExerciseItemInfo(exerciseName)

        _isExerciseItemInfoPrimium.value = exerciseItemInfoModel.exerciseItemInfo.isPrimium

        _exerciseItemIndex.value = exerciseItemInfoModel.exerciseIndex

        _exerciseItemInfoContent.value = exerciseItemInfoModel.exerciseItemInfo.exerciseContent0

        updateExerciseItemInfo()

    }


    // 인덱스에 따른 내용 바꿔주는 메서드
    private fun updateExerciseItemInfo() {
        val index = exerciseItemInfoModel.exerciseIndex
        _exerciseItemInfoContent.value = when (index) {
            0 -> exerciseItemInfoModel.exerciseItemInfo.exerciseContent0
            else -> exerciseItemInfoModel.exerciseItemInfo.exerciseContent1
        }
    }

    fun getExerciseItemInfo(): ExerciseItemInfo {

        return exerciseItemInfoModel.exerciseItemInfo

    } // getExerciseItemInfo()


    // index + 1
    fun forwardExerciseItemInfoIndex() {

        exerciseItemInfoModel.exerciseIndex = 1

        _exerciseItemIndex.value = exerciseItemInfoModel.exerciseIndex
        updateExerciseItemInfo()

    } // addExerciseItemInfoIndex()


    // index - 1
    fun backExerciseItemInfoIndex() {

        exerciseItemInfoModel.exerciseIndex -= 1
        _exerciseItemIndex.value = exerciseItemInfoModel.exerciseIndex
        updateExerciseItemInfo()

    } // backExerciseItemInfoIndex()

    fun setExerciseItemIndex(index: Int) {

        if (_exerciseItemIndex.value != index) {
            exerciseItemInfoModel.exerciseIndex = index
            _exerciseItemIndex.value = index
            updateExerciseItemInfo()
        }

    } // setExerciseItemIndex()

}