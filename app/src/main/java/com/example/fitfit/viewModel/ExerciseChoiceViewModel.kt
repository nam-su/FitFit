package com.example.fitfit.viewModel

import androidx.lifecycle.ViewModel
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.model.ExerciseChoiceModel

class ExerciseChoiceViewModel: ViewModel(){

    private val exerciseChoiceModel = ExerciseChoiceModel()


    // 모델의 exerciseChoiceList를 가져오는 메서드.
    fun setRecyclerViewExerciseChoice(): ArrayList<PoseExercise>{

        return exerciseChoiceModel.setExerciseChoiceList()

    } // setRecyclerViewExerciseChoice()


    // 모델에 poseExercise 객체 쉐어드에 저장하는 메서드 호출
    fun setPoseExercise(poseExercise: PoseExercise): String {

        exerciseChoiceModel.setPoseExercise(poseExercise)

        return poseExercise.exerciseName

    } // savePoseExercise()


}