package com.example.fitfit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fitfit.R
import com.example.fitfit.adapter.ExerciseChoiceAdapter
import com.example.fitfit.databinding.FragmentExerciseChoiceBinding
import com.example.fitfit.viewModel.ExerciseChoiceViewModel

class ExerciseChoiceFragment : Fragment() {

    lateinit var binding: FragmentExerciseChoiceBinding
    lateinit var exerciseChoiceViewModel: ExerciseChoiceViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise_choice,container,false)

        return binding.root

    } // onCreateView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setVariable()

    } // onViewCreated

    // 변수 초기화
    fun setVariable() {

        exerciseChoiceViewModel = ExerciseChoiceViewModel()
        binding.recyclerViewExerciseChoice.adapter = ExerciseChoiceAdapter(exerciseChoiceViewModel.setRecyclerViewExerciseChoice())

    } // setVariable

}