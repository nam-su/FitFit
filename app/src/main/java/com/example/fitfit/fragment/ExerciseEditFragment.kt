package com.example.fitfit.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ExerciseChoiceAdapter
import com.example.fitfit.adapter.PoseExerciseAdapter
import com.example.fitfit.databinding.FragmentExerciseEditBinding
import com.example.fitfit.model.ExerciseEditModel
import com.example.fitfit.viewModel.ExerciseEditViewModel


class ExerciseEditFragment : Fragment() {

    val TAG = "운동 편집 프래그먼트"

    private lateinit var binding: FragmentExerciseEditBinding
    private lateinit var exerciseEditViewModel: ExerciseEditViewModel

    private lateinit var myPoseExerciseAdapter: PoseExerciseAdapter
    private lateinit var allSquatAdapter: PoseExerciseAdapter
    private lateinit var allPushUpAdapter: PoseExerciseAdapter
    private lateinit var allLungeAdapter: PoseExerciseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise_edit,container,false)

        return binding.root

    } // onCreateView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setClickListener()

    } // onViewCreated


    // 변수 초기화 메서드
    private fun setVariable() {

        // 뷰모델 초기화
        exerciseEditViewModel = ExerciseEditViewModel()

        // 내 운동 리스트 리사이클러뷰
        myPoseExerciseAdapter =
            PoseExerciseAdapter(exerciseEditViewModel.setMyExerciseList(),true,"true")

        binding.recyclerViewMyPoseExerciseList.adapter = myPoseExerciseAdapter


        // 모든 스쿼트 리스트 리사이클러뷰
        allSquatAdapter =
            PoseExerciseAdapter(exerciseEditViewModel.setAllSquatList(),true,"false")

        binding.recyclerViewAllSquat.adapter = allSquatAdapter

        // 모든 스쿼트 리스트 리사이클러뷰
        allPushUpAdapter =
            PoseExerciseAdapter(exerciseEditViewModel.setAllPushUpList(),true,"false")

        binding.recyclerViewAllPushUp.adapter = allPushUpAdapter


        // 모든 스쿼트 리스트 리사이클러뷰
        allLungeAdapter =
            PoseExerciseAdapter(exerciseEditViewModel.setAllLungeList(),true,"false")

        binding.recyclerViewAllLunge.adapter = allLungeAdapter


    } // setVariable()


    // 클릭 리스너 초기화 하는 메서드
    private fun setClickListener() {

        // 뒤로가기 버튼 눌렀을 때
        binding.imageButtonEditBack.setOnClickListener {

            findNavController().popBackStack()
            (activity as MainActivity).visibleBottomNavi()

        }

        myPoseExerciseAdapter.exerciseEditItemDeleteButtonClick = object :PoseExerciseAdapter.ExerciseEditItemDeleteButtonClick{

            override fun onDeleteButtonClick(view: View, position: Int) {

                exerciseEditViewModel.deleteExerciseItem(myPoseExerciseAdapter.poseExerciseList,position)
//                myPoseExerciseAdapter.poseExerciseList.removeAt(position)
                myPoseExerciseAdapter.notifyDataSetChanged()

            }

        }

        allSquatAdapter.exerciseEditItemAddButtonClick = object :PoseExerciseAdapter.ExerciseEditItemAddButtonClick{

            override fun onAddButtonClick(view: View, position: Int) {
                TODO("Not yet implemented")
            }

        }

        allPushUpAdapter.exerciseEditItemAddButtonClick = object :PoseExerciseAdapter.ExerciseEditItemAddButtonClick{

            override fun onAddButtonClick(view: View, position: Int) {
                TODO("Not yet implemented")
            }

        }

        allLungeAdapter.exerciseEditItemAddButtonClick = object :PoseExerciseAdapter.ExerciseEditItemAddButtonClick{

            override fun onAddButtonClick(view: View, position: Int) {
                TODO("Not yet implemented")
            }

        }

    } // setClickListener()

}