package com.example.fitfit.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ExerciseChoiceAdapter
import com.example.fitfit.databinding.FragmentExerciseChoiceBinding
import com.example.fitfit.viewModel.ExerciseChoiceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseChoiceFragment : Fragment() {

    private val TAG = "운동 선택 프래그먼트"

    lateinit var binding: FragmentExerciseChoiceBinding
    lateinit var exerciseChoiceViewModel: ExerciseChoiceViewModel
    lateinit var exerciseChoiceAdapter: ExerciseChoiceAdapter

    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach

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

                // 뷰모델을 통해서 쉐어드에 저장하는 메서드 호출 , 이름값 리턴 후 동작인식 프래그먼트로 전송
                val exerciseName = exerciseChoiceAdapter.exerciseChoiceList[position].exerciseName

                val bundle = bundleOf("exerciseName" to exerciseName)

                view.findNavController().navigate(R.id.action_exerciseChoiceFragment_to_poseDetectionFragment,bundle)

            }
        }


        // 운동 리스트 편집 클릭 리스너
        binding.textViewEditExerciseList.setOnClickListener {

            it.findNavController().navigate(R.id.action_exerciseChoiceFragment_to_exerciseEditFragment)
            (activity as MainActivity).goneBottomNavi()

        }

        // 뒤로가기 버튼 클릭
        binding.imageButtonBackExerciseChoice.setOnClickListener {

            it.findNavController().popBackStack()
            (activity as MainActivity).visibleBottomNavi()

        }

    } // setClickListener()


    override fun onResume() {
        super.onResume()
        exerciseChoiceAdapter.notifyDataSetChanged()
        exerciseChoiceAdapter.exerciseChoiceList.forEach {
            Log.d(TAG, "onResume: ${it.exerciseName}")
        }
    }

    // 뒤로가기 눌렀을때
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                findNavController().popBackStack()
                (activity as MainActivity).visibleBottomNavi()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    } // onBackPressed()

}