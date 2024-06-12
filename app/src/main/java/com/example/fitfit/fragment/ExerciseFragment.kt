package com.example.fitfit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ExerciseDetailViewAdapter
import com.example.fitfit.adapter.PoseExerciseAdapter
import com.example.fitfit.data.ExerciseInfo
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

        binding.recyclerViewTodayExercise.adapter = PoseExerciseAdapter(exerciseViewModel.getMyExerciseList(),false,"")

        binding.recyclerViewMyExerciseInfo.adapter = ExerciseDetailViewAdapter(exerciseViewModel.getMyExerciseInfoList())

    } // setVariable()


    // 클릭리스너 초기화
    private fun setClickListener() {

        binding.buttonStartTodayExercise.setOnClickListener {

            it.findNavController().navigate(R.id.action_exerciseFragment_to_exerciseChoiceFragment)
            (activity as MainActivity).goneBottomNavi()

        }

        binding.textViewAllExercise.setOnClickListener {

            it.findNavController().navigate(R.id.action_exerciseFragment_to_payFragment)
            (activity as MainActivity).goneBottomNavi()

        }

        binding.textViewDetailView.setOnClickListener {

            binding.constraintLayoutDetailView.visibility = View.VISIBLE
            binding.textViewDetailView.visibility = View.GONE

        }

        binding.textViewQuickView.setOnClickListener {

            binding.constraintLayoutDetailView.visibility = View.GONE
            binding.textViewDetailView.visibility = View.VISIBLE

        }

    } // setClickListener()

}