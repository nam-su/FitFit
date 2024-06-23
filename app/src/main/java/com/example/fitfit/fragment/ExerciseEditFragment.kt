package com.example.fitfit.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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

    private lateinit var callback: OnBackPressedCallback

    private lateinit var myPoseExerciseAdapter: PoseExerciseAdapter
    private lateinit var allSquatAdapter: PoseExerciseAdapter
    private lateinit var allPushUpAdapter: PoseExerciseAdapter
    private lateinit var allLungeAdapter: PoseExerciseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise_edit,container,false)

        // 뷰모델 초기화
        exerciseEditViewModel = ExerciseEditViewModel()

        // xml 파일에 뷰모델 연결
        binding.exerciseEditViewModel = exerciseEditViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root

    } // onCreateView()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setClickListener()
        setObserve()

    } // onViewCreated


    // 변수 초기화 메서드
    private fun setVariable() {

        // 내 운동 리스트 리사이클러뷰
        myPoseExerciseAdapter =
            PoseExerciseAdapter(exerciseEditViewModel.getSharedMyExerciseList(),true,"true")

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


    // observe하는 메서드
    private fun setObserve() {

        // 최대 사이즈 감지 후 토스트
        exerciseEditViewModel.checkMyExerciseListSizeMax.observe(viewLifecycleOwner) {

            if(!it) {

                Toast.makeText(requireContext(),"최대 20개 이상의 운동까지 추가 가능합니다.",Toast.LENGTH_SHORT).show()

            }

        }

    } // setObserve


    // 클릭 리스너 초기화 하는 메서드
    private fun setClickListener() {

        // 뒤로가기 버튼 눌렀을 때
        binding.imageButtonEditBack.setOnClickListener {

            findNavController().popBackStack()

        }


        // 내 운동리스트에서 삭제 버튼 눌렀을 때
        myPoseExerciseAdapter.exerciseEditItemDeleteButtonClick = object :PoseExerciseAdapter.ExerciseEditItemDeleteButtonClick{

            override fun onDeleteButtonClick(view: View, position: Int) {

                exerciseEditViewModel.deleteExerciseItem(myPoseExerciseAdapter.poseExerciseList,position)
                myPoseExerciseAdapter.notifyDataSetChanged()
                allSquatAdapter.notifyDataSetChanged()
                allPushUpAdapter.notifyDataSetChanged()
                allLungeAdapter.notifyDataSetChanged()

            }

        }


        // 스쿼트리스트에서 추가 버튼 눌렀을 때
        allSquatAdapter.exerciseEditItemAddButtonClick = object :PoseExerciseAdapter.ExerciseEditItemAddButtonClick{

            override fun onAddButtonClick(view: View, position: Int) {

                exerciseEditViewModel.addExerciseItem(allSquatAdapter.poseExerciseList,position)
                myPoseExerciseAdapter.notifyDataSetChanged()
                allSquatAdapter.notifyDataSetChanged()

            }

        }


        // 푸시업 리스트에서 추가버튼 눌렀을 때
        allPushUpAdapter.exerciseEditItemAddButtonClick = object :PoseExerciseAdapter.ExerciseEditItemAddButtonClick{

            override fun onAddButtonClick(view: View, position: Int) {

                exerciseEditViewModel.addExerciseItem(allPushUpAdapter.poseExerciseList,position)
                myPoseExerciseAdapter.notifyDataSetChanged()
                allPushUpAdapter.notifyDataSetChanged()

            }

        }


        // 런지 리스트에서 추가버튼 눌렀을 때
        allLungeAdapter.exerciseEditItemAddButtonClick = object :PoseExerciseAdapter.ExerciseEditItemAddButtonClick{

            override fun onAddButtonClick(view: View, position: Int) {

                exerciseEditViewModel.addExerciseItem(allLungeAdapter.poseExerciseList,position)
                myPoseExerciseAdapter.notifyDataSetChanged()
                allLungeAdapter.notifyDataSetChanged()

            }

        }


        // 리스트 편집 후 완료 버튼 눌렀을 때
        binding.textViewEditComplete.setOnClickListener {

            if(!exerciseEditViewModel.checkMyExerciseListSizeMin()) {

                Toast.makeText(requireContext(),"최소 3개 이상의 운동이 있어야 합니다.",Toast.LENGTH_SHORT).show()

            } else {

                // 여기에 서버와 통신해서 리스트 갱신해주는 메서드 필요.

                //리스트 불러오기
                Log.d(TAG, "setClickListener: ${exerciseEditViewModel.getExerciseList().size}")

            }

        }



    } // setClickListener()


    // 뒤로가기 버튼 클릭 했을 때
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                findNavController().popBackStack()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    } // setOnBackPressed()

}