package com.example.fitfit.viewModel

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
        _exerciseItemIndex.value = exerciseItemInfoModel.exerciseIndex
        _exerciseItemInfoContent.value = exerciseItemInfoModel.exerciseItemInfo.exerciseContent0
        setExerciseItemInfoImage()

    }


    // 인덱스에 따른 이미지 바꿔주는 메서드
    fun setExerciseItemInfoImage(): Int {

        return when(exerciseItemInfoModel.exerciseIndex) {

            0 -> exerciseItemInfoModel.exerciseItemInfo.indexImage0
            else -> exerciseItemInfoModel.exerciseItemInfo.indexImage1

        }

    } // setExerciseItemInfoImage()


    // index + 1
    fun forwardExerciseItemInfoIndex() {

        exerciseItemInfoModel.exerciseIndex = 1

        _exerciseItemIndex.value = exerciseItemInfoModel.exerciseIndex
        _exerciseItemInfoContent.value = exerciseItemInfoModel.exerciseItemInfo.exerciseContent1

    } // addExerciseItemInfoIndex()


    // index - 1
    fun backExerciseItemInfoIndex() {

        exerciseItemInfoModel.exerciseIndex -= 1

        _exerciseItemIndex.value = exerciseItemInfoModel.exerciseIndex
        _exerciseItemInfoContent.value = exerciseItemInfoModel.exerciseItemInfo.exerciseContent0

    } // backExerciseItemInfoIndex()

}