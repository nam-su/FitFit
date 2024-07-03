package com.example.fitfit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ChallengeAdapter
import com.example.fitfit.adapter.ExerciseDetailViewAdapter
import com.example.fitfit.adapter.ExerciseItemInfoAdapter
import com.example.fitfit.adapter.PoseExerciseAdapter
import com.example.fitfit.data.ExerciseInfo
import com.example.fitfit.databinding.FragmentExerciseBinding
import com.example.fitfit.viewModel.ExerciseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseFragment : Fragment() {

    lateinit var binding: FragmentExerciseBinding
    private val exerciseViewModel: ExerciseViewModel by viewModels()
    var currentPage  = 0

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
        startAutoScroll()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        binding.exerciseViewModel = exerciseViewModel

        //내 운동리스트
        binding.recyclerViewTodayExercise.adapter = PoseExerciseAdapter(exerciseViewModel.getMyExerciseList(),false,"")

        //자세히보기
        binding.recyclerViewMyExerciseInfo.adapter = ExerciseDetailViewAdapter(exerciseViewModel.getMyExerciseInfoList())

        //뷰페이저 어댑터
        binding.viewPager.adapter = ChallengeAdapter(exerciseViewModel.getChallengeList(), requireContext(), exerciseViewModel)

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

    } // setClickListener()


    // 자동 스크롤
    private fun startAutoScroll() {

        lifecycleScope.launch {

            while (true) {

                delay(5000) // 3초 대기

                    if (currentPage == binding.viewPager.adapter?.itemCount ?: 0 - 1) { // 마지막 페이지 확인

                        currentPage = 0

                        binding.viewPager.setCurrentItem(currentPage, true)

                    } else {

                        currentPage++

                        binding.viewPager.setCurrentItem(currentPage, true)

                    }

                }

        }
    }


}