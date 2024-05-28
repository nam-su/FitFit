package com.example.fitfit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ExerciseChoiceAdapter
import com.example.fitfit.databinding.FragmentExerciseChoiceBinding
import com.example.fitfit.viewModel.ExerciseChoiceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseChoiceFragment : Fragment() {

    lateinit var binding: FragmentExerciseChoiceBinding
    lateinit var exerciseChoiceViewModel: ExerciseChoiceViewModel
    lateinit var exerciseChoiceAdapter: ExerciseChoiceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise_choice,container,false)

        return binding.root

    } // onCreateView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setClickListener()

    } // onViewCreated

    // 변수 초기화
    fun setVariable() {

        exerciseChoiceViewModel = ExerciseChoiceViewModel()
        exerciseChoiceAdapter = ExerciseChoiceAdapter(exerciseChoiceViewModel.setRecyclerViewExerciseChoice())
        binding.recyclerViewExerciseChoice.adapter = exerciseChoiceAdapter


    } // setVariable


    // 클릭 리스너 초기화
    private fun setClickListener() {

        // 운동 선택 아이템 클릭 리스너
        exerciseChoiceAdapter.exerciseChoiceItemClick = object :ExerciseChoiceAdapter.ExerciseChoiceItemClick{
            // 아이템 클릭 시 동작 인식 프래그먼트로 전환.
            override fun onClick(view: View, position: Int) {

                val exerciseName = exerciseChoiceAdapter.exerciseChoiceList[position].exerciseName

                val bundle = bundleOf("exerciseName" to exerciseName)

                view.findNavController().navigate(R.id.action_exerciseChoiceFragment_to_poseDetectionFragment,bundle)

            }
        }

    } // setClickListener()

}