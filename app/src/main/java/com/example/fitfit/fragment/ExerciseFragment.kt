package com.example.fitfit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentExerciseBinding
import com.example.fitfit.viewModel.ExerciseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseFragment : Fragment() {

    lateinit var binding: FragmentExerciseBinding
    lateinit var exerciseViewModel: ExerciseViewModel

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise,container,false)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setClickListener()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        exerciseViewModel = ExerciseViewModel()
        binding.exerciseViewModel = exerciseViewModel

    } // setVariable()


    private fun setClickListener() {

        binding.buttonStartTodayExercise.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {(activity as MainActivity).changeExerciseToPoseDetectionFragment()}

        }

    }

}