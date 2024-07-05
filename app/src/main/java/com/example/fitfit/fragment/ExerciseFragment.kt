package com.example.fitfit.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ChallengeAdapter
import com.example.fitfit.adapter.ExerciseChoiceAdapter
import com.example.fitfit.adapter.ExerciseDetailViewAdapter
import com.example.fitfit.adapter.ExerciseItemInfoAdapter
import com.example.fitfit.adapter.PoseExerciseAdapter
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.ExerciseInfo
import com.example.fitfit.databinding.CustomDialogTwoButtonBinding
import com.example.fitfit.databinding.FragmentExerciseBinding
import com.example.fitfit.viewModel.ExerciseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseFragment : Fragment() {
    
    lateinit var binding: FragmentExerciseBinding
    lateinit var challengeAdapter: ChallengeAdapter
    lateinit var customDialogBinding: CustomDialogTwoButtonBinding
    lateinit var dialog: AlertDialog

    private val exerciseViewModel: ExerciseViewModel by viewModels()
    var currentPage  = 0

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise,container,false)
        customDialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_two_button, null, false)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setClickListener()
        startAutoScroll()
        setObserve()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        binding.exerciseViewModel = exerciseViewModel

        //내 운동리스트
        binding.recyclerViewTodayExercise.adapter = PoseExerciseAdapter(exerciseViewModel.getMyExerciseList(),false,"")

        //자세히보기
        binding.recyclerViewMyExerciseInfo.adapter = ExerciseDetailViewAdapter(exerciseViewModel.getMyExerciseInfoList())

        //뷰페이저 어댑터
        challengeAdapter = ChallengeAdapter(exerciseViewModel.getChallengeList(), requireContext(), exerciseViewModel)
        binding.viewPager.adapter = challengeAdapter

        //인디케이터
        binding.dotsIndicator.setViewPager2(binding.viewPager)


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

        
        challengeAdapter.challengeItemClick  = object :ChallengeAdapter.ChallengeItemClick{
            override fun onClick(view: View, challenge: Challenge) {
                showCustomDialog(challenge)
            }

        } 
        
        
        

    } // setClickListener()


    // 뷰모델 관찰
    private fun setObserve(){

        exerciseViewModel.joinResult.observe(viewLifecycleOwner) {

            when(it){
                "already" -> Toast.makeText(requireContext(), "이미 해당 챌린지에 참여 중입니다.", Toast.LENGTH_SHORT).show()
                "success" -> {
                    Toast.makeText(requireContext(), "해당 챌린지에 참여를 시작합니다.", Toast.LENGTH_SHORT).show()
                    binding.viewPager.adapter!!.notifyDataSetChanged()
                    this.findNavController().navigate(R.id.action_exerciseFragment_to_DiaryFragment)
                }
                else -> Toast.makeText(requireContext(), "네트워크 연결 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }

        }

    }


    // 자동 스크롤
    private fun startAutoScroll() {

        lifecycleScope.launch {

            while (true) {

                delay(5000) // 3초 대기

                    if (currentPage == (binding.viewPager.adapter?.itemCount ?: (0 - 1))) { // 마지막 페이지 확인

                        currentPage = 0

                        binding.viewPager.setCurrentItem(currentPage, true)

                    } else {

                        currentPage++

                        binding.viewPager.setCurrentItem(currentPage, true)

                    }

                }

        }
    } // startAutoScroll()


    //커스텀 다이얼로그 띄우기
    private fun showCustomDialog(challenge: Challenge){

        //기존의 바인딩 객체가 이미 부모뷰에 속해있는지 확인하고 제거하기
        //제거하지 않으면 부모뷰의 충돌문제로 오류가 뜸.
        customDialogBinding.root.parent?.let { parent ->
            (parent as ViewGroup).removeView(customDialogBinding.root)
        }

        dialog = AlertDialog.Builder(context)
            .setView(customDialogBinding.root)
            .setCancelable(false)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customDialogBinding.textViewContent.text = challenge.detail
        customDialogBinding.textViewTitle.text = challenge.challengeName
        customDialogBinding.textViewButtonOk.text = "참여"
        customDialogBinding.textViewButtonOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))

        dialog.show()

        //다이얼로그 취소 버튼 클릭
        customDialogBinding.textViewCancel.setOnClickListener {
            dialog.dismiss()
        }

        //다이얼로그 참여 버튼 클릭
        customDialogBinding.textViewButtonOk.setOnClickListener {

            /**서버와 통신**/
            exerciseViewModel.challengeJoin(challenge)

            dialog.dismiss()
            
        }

    }



}