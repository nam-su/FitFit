package com.example.fitfit.fragment

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ChallengeRankAdapter
import com.example.fitfit.adapter.CheckWeekExerciseAdapter
import com.example.fitfit.adapter.PoseExerciseAdapter
import com.example.fitfit.adapter.PoseExerciseGridAdapter
import com.example.fitfit.databinding.FragmentHomeBinding
import com.example.fitfit.function.GridSpacingItemDecoration
import com.example.fitfit.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var homeViewModel: HomeViewModel

    private lateinit var callback: OnBackPressedCallback

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)

        return binding.root

    } // onCreateView()


    // onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setClickListener()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        homeViewModel = HomeViewModel()
        binding.homeViewModel = homeViewModel

        // 일주일 동안 운동양 체크하는 리사이클러뷰 어댑터
        binding.recyclerViewCheckWeekExercise.layoutManager = GridLayoutManager(activity?.applicationContext,7)
        binding.recyclerViewCheckWeekExercise.adapter = CheckWeekExerciseAdapter(homeViewModel.setRecyclerViewWeekStatus())

        // 홈 프래그먼트에서 보이는 랭킹 어댑터
        binding.recyclerViewChallengeRank.adapter = ChallengeRankAdapter(homeViewModel.setRecyclerViewPagedChallengeRank())

        // 홈 프래그먼트에서 보이는 운동리스트 어댑터
        binding.recyclerViewPagedAllExercise.adapter = PoseExerciseAdapter(homeViewModel.setRecyclerViewAllExercise(),false,"")
        binding.recyclerViewPagedAllExercise.layoutManager = LinearLayoutManager(activity?.applicationContext,LinearLayoutManager.HORIZONTAL,false)

        // 운동 전체보기 리사이클러뷰 레이아웃 메니저 설정
        binding.recyclerViewAllExercise.layoutManager = GridLayoutManager(activity?.applicationContext,4)
        binding.recyclerViewAllExercise.addItemDecoration(GridSpacingItemDecoration(4,(10f * Resources.getSystem().displayMetrics.density).toInt()))

        // 운동 전체보기 어댑터
        binding.recyclerViewAllExercise.adapter = PoseExerciseGridAdapter(homeViewModel.setRecyclerViewAllExercise())

        // 랭킹 모두보기 어댑터
        binding.recyclerViewAllChallengeRank.adapter = ChallengeRankAdapter(homeViewModel.setRecyclerViewAllChallengeRank())

        homeViewModel.selectUserExercise()

        // 시작할때 통신을해서 viewModel에 어레이리스트 생성 후 observe해서 어뎁터 리스트에 꽂아준다?

    } // setVariable()


    // 클릭 리스너 초기화
    private fun setClickListener() {

        // 운동 전체보기 클릭 리스너
        binding.textViewViewAllExercise.setOnClickListener{

            binding.constraintLayoutHome.visibility = View.GONE
            binding.constraintLayoutAllExercise.visibility = View.VISIBLE

        }


        // 챌린지 랭킹 전체보기 클릭 리스너
        binding.textViewViewAllChallenge.setOnClickListener {

            binding.constraintLayoutHome.visibility = View.GONE
            binding.constraintLayoutAllChallenge.visibility = View.VISIBLE

        }


        // 챌린지 랭킹 에서 뒤로가기 버튼 클릭 리스너
        binding.imageButtonBackAllChallenge.setOnClickListener {

            binding.constraintLayoutAllChallenge.visibility = View.GONE
            binding.constraintLayoutHome.visibility = View.VISIBLE

        }

        // 모든 운동보기 에서 뒤로가기 버튼 클릭리스너
        binding.imageButtonBackToHome.setOnClickListener{

            binding.constraintLayoutHome.visibility = View.VISIBLE
            binding.constraintLayoutAllExercise.visibility = View.GONE

        }


        // 구독하기 버튼 클릭 리스너
        binding.buttonSubscribe.setOnClickListener {

            it.findNavController().navigate(R.id.payFragment)
            (activity as MainActivity).goneBottomNavi()

        }


    } // setOnClickListener()


    // 뒤로가기 클릭 리스너
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                // 운동 전체보기 페이지에서 뒤로가기 눌렀을 때
                if (binding.constraintLayoutAllExercise.visibility == View.VISIBLE) {

                    binding.constraintLayoutHome.visibility = View.VISIBLE
                    binding.constraintLayoutAllExercise.visibility = View.GONE

                }

                // 전체 챌린지 랭킹페이지에서 뒤로가기 눌렀을 때
                else if(binding.constraintLayoutAllChallenge.visibility == View.VISIBLE) {

                    binding.constraintLayoutHome.visibility = View.VISIBLE
                    binding.constraintLayoutAllChallenge.visibility = View.GONE

                // 다른 상황에서 뒤로가기 눌렀을 때
                } else {

                    (activity as MainActivity).finish()

                }

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    }

}