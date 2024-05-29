package com.example.fitfit.viewModel

import androidx.lifecycle.ViewModel
import com.example.fitfit.data.ExerciseChoice
import com.example.fitfit.model.ExerciseChoiceModel

class ExerciseChoiceViewModel: ViewModel(){

    private val exerciseChoiceModel = ExerciseChoiceModel()


    // 모델의 exerciseChoiceList를 가져오는 메서드.
    fun setRecyclerViewExerciseChoice(): ArrayList<ExerciseChoice>{

        return exerciseChoiceModel.setExerciseChoiceList()

    } // setRecyclerViewExerciseChoice()

}