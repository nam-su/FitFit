package com.example.fitfit.fragment

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ChallengeJoinAdapter
import com.example.fitfit.adapter.ChallengeRankAdapter
import com.example.fitfit.adapter.CheckWeekExerciseAdapter
import com.example.fitfit.adapter.PoseExerciseAdapter
import com.example.fitfit.adapter.PoseExerciseGridAdapter
import com.example.fitfit.data.Rank
import com.example.fitfit.databinding.FragmentHomeBinding
import com.example.fitfit.function.GridSpacingItemDecoration
import com.example.fitfit.viewModel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val TAG = "홈 프래그먼트"

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
        setRankingRecyclerViewAndAdapter()
        setObserve()
        setClickListener()


    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        homeViewModel = HomeViewModel()
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        // 일주일 동안 운동양 체크하는 리사이클러뷰 어댑터
        binding.recyclerViewCheckWeekExercise.layoutManager = GridLayoutManager(activity?.applicationContext,7)
        binding.recyclerViewCheckWeekExercise.adapter = CheckWeekExerciseAdapter(homeViewModel.setRecyclerViewWeekStatus())


        // 홈 프래그먼트에서 보이는 운동리스트 어댑터
        binding.recyclerViewPagedAllExercise.adapter = PoseExerciseAdapter(homeViewModel.setRecyclerViewAllExercise(),false,"")
        binding.recyclerViewPagedAllExercise.layoutManager = LinearLayoutManager(activity?.applicationContext,LinearLayoutManager.HORIZONTAL,false)

        // 운동 전체보기 리사이클러뷰 레이아웃 메니저 설정
        binding.recyclerViewAllExercise.layoutManager = GridLayoutManager(activity?.applicationContext,4)
        binding.recyclerViewAllExercise.addItemDecoration(GridSpacingItemDecoration(4,(10f * Resources.getSystem().displayMetrics.density).toInt()))

        // 운동 전체보기 어댑터
        binding.recyclerViewAllExercise.adapter = PoseExerciseGridAdapter(homeViewModel.setRecyclerViewAllExercise())



    } // setVariable()


    // 리사이클러뷰 세팅
    private fun setRankingRecyclerViewAndAdapter(){
        // 홈 프래그먼트에서 보이는 랭킹 어댑터
        lifecycleScope.launch {
            val rankingList = homeViewModel.getRankingListToServer()
            val homeRankingList = rankingList.partition { rank -> rank.ranking < 4 }.first as ArrayList
            rankingList?.let {
                //홈에서 보이는 랭킹 어댑터
                binding.recyclerViewChallengeRank.adapter = ChallengeRankAdapter(homeRankingList, homeViewModel, context)

            }

            // 홈 프래그먼트에서 보이는 랭킹 어댑터
            rankingList?.let {
                // 랭킹 모두보기 어댑터
                binding.recyclerViewAllChallengeRank.adapter = ChallengeRankAdapter(it, homeViewModel, context)
            }


            //챌린지 랭킹 아이템 클릭 리스너
            (binding.recyclerViewAllChallengeRank.adapter as ChallengeRankAdapter).challengeRankItemClick = object: ChallengeRankAdapter.ChallengeRankItemClick {
                override fun onClick(view: View, rank: Rank) {
                    Log.d(TAG, "onClick: ${rank.id}")
                }

            }

        }
    }


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

        // 운동 아이템 클릭 리스너
        (binding.recyclerViewPagedAllExercise.adapter as PoseExerciseAdapter).exerciseItemClick = object: PoseExerciseAdapter.ExerciseItemClick {

            override fun onExerciseItemClick(view: View, position: Int) {

                // 운동이름을 번들로 넘긴다.
                val exerciseName = (binding.recyclerViewPagedAllExercise.adapter as PoseExerciseAdapter).poseExerciseList[position].exerciseName
                val bundle = bundleOf("exerciseName" to exerciseName)

                findNavController().navigate(R.id.exerciseItemInfoFragment,bundle)
                (activity as MainActivity).goneBottomNavi()

            }

        }


        // 챌린지 랭킹 텍스트 클릭 리스너
        binding.linearLayoutRanking.setOnClickListener {

            val bottomSheetChallengeListFragment = BottomSheetChallengeListFragment(homeViewModel)
            bottomSheetChallengeListFragment.show(parentFragmentManager,"")

        }


    } // setOnClickListener()


    //observe 설정
    private fun setObserve() {

        homeViewModel.challengeName.observe(viewLifecycleOwner){
            setRankingRecyclerViewAndAdapter()
        }

    }


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