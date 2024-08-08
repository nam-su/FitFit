package com.example.fitfit.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ChallengeJoinAdapter
import com.example.fitfit.adapter.ChallengeRankAdapter
import com.example.fitfit.adapter.CheckWeekExerciseAdapter
import com.example.fitfit.adapter.PoseExerciseAdapter
import com.example.fitfit.adapter.PoseExerciseGridAdapter
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.Rank
import com.example.fitfit.databinding.CustomDialogChallengeRankingBinding
import com.example.fitfit.databinding.CustomDialogNetworkDisconnectBinding
import com.example.fitfit.databinding.FragmentHomeBinding
import com.example.fitfit.function.GridSpacingItemDecoration
import com.example.fitfit.function.MyApplication
import com.example.fitfit.viewModel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val TAG = "홈 프래그먼트"

    lateinit var binding: FragmentHomeBinding
    lateinit var homeViewModel: HomeViewModel

    lateinit var customDialogBinding: CustomDialogChallengeRankingBinding

    lateinit var customNetworkDialogBinding: CustomDialogNetworkDisconnectBinding

    lateinit var top3ChallengeRankAdapter: ChallengeRankAdapter
    lateinit var totalChallengeRankAdapter: ChallengeRankAdapter

    private lateinit var callback: OnBackPressedCallback


    // onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        customNetworkDialogBinding = DataBindingUtil.inflate(inflater,R.layout.custom_dialog_network_disconnect,null,false)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setObserve()
        setListener()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        (activity as MainActivity).visibleBottomNavi()

        homeViewModel = HomeViewModel()
        binding.homeViewModel = homeViewModel

        binding.lifecycleOwner = this

        top3ChallengeRankAdapter = ChallengeRankAdapter(arrayListOf(),homeViewModel)
        totalChallengeRankAdapter = ChallengeRankAdapter(arrayListOf(),homeViewModel)


        // 일주일 동안 운동양 체크하는 리사이클러뷰 어댑터
        binding.recyclerViewCheckWeekExercise.layoutManager = GridLayoutManager(activity?.applicationContext,7)
        binding.recyclerViewCheckWeekExercise.adapter = CheckWeekExerciseAdapter(homeViewModel.setRecyclerViewWeekStatus())

        homeViewModel.setWeekStatus()

        // 홈 프래그먼트에서 보이는 운동리스트 어댑터
        binding.recyclerViewPagedAllExercise.adapter = PoseExerciseAdapter(homeViewModel.getBasicExerciseList(),false,"")
        binding.recyclerViewPagedAllExercise.layoutManager = LinearLayoutManager(activity?.applicationContext,LinearLayoutManager.HORIZONTAL,false)

        // 운동 전체보기 리사이클러뷰 레이아웃 메니저 설정
        binding.recyclerViewAllExercise.layoutManager = GridLayoutManager(activity?.applicationContext,4)
        binding.recyclerViewAllExercise.addItemDecoration(GridSpacingItemDecoration(4,(10f * Resources.getSystem().displayMetrics.density).toInt()))

        // 운동 전체보기 어댑터
        binding.recyclerViewAllExercise.adapter = PoseExerciseGridAdapter(homeViewModel.getBasicExerciseList())

    } // setVariable()


    // 리사이클러뷰 세팅
    private fun setRankingRecyclerViewAndAdapter() {

        // 홈 프래그먼트에서 보이는 랭킹 어댑터
        lifecycleScope.launch {

            var rankingList = ArrayList<Rank>()

            // 인터넷 연결 x
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            // 인터넷 연결 o
            } else {

                rankingList = homeViewModel.getRankingListToServer()

            }

            rankingList.let { list ->

                val homeRankingList = list.partition { rank -> rank.ranking < 4 }.first as ArrayList
                top3ChallengeRankAdapter = ChallengeRankAdapter(homeRankingList, homeViewModel)
                totalChallengeRankAdapter = ChallengeRankAdapter(list, homeViewModel)

                // 홈에서 보이는 랭킹 어댑터 설정
                binding.recyclerViewChallengeRank.adapter = top3ChallengeRankAdapter

                // 랭킹 모두 보기 어댑터 설정
                binding.recyclerViewAllChallengeRank.adapter = totalChallengeRankAdapter
                binding.recyclerViewAllChallengeRank.smoothScrollToPosition(totalChallengeRankAdapter.itemCount - 1)


            }

            // 챌린지 랭킹 아이템 클릭 리스너 설정
            top3ChallengeRankAdapter.challengeRankItemClick =
                object : ChallengeRankAdapter.ChallengeRankItemClick {

                    override fun onClick(view: View, rank: Rank) {
                        Log.d(TAG, "onClick: ${rank.id}")

                        // 인터넷 연결 안되어 있는 경우
                        if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                            setNetworkCustomDialog()

                            // 인터넷 연결이 되어 있는 경우
                        } else {

                            lifecycleScope.launch {

                                // 인터넷 연결 x
                                if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                                    setNetworkCustomDialog()

                                    // 인터넷 연결 o
                                } else {

                                    setCustomDialog(rank, homeViewModel.getMyChallengeListToServer(rank.id))

                                }

                            }

                        }

                    }

                }

            // 챌린지 랭킹 아이템 클릭 리스너 설정
            totalChallengeRankAdapter.challengeRankItemClick = object : ChallengeRankAdapter.ChallengeRankItemClick {
                override fun onClick(view: View, rank: Rank) {
                    Log.d(TAG, "onClick: ${rank.id}")

                    lifecycleScope.launch {

                        // 인터넷 연결 x
                        if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                            setNetworkCustomDialog()

                            // 인터넷 연결 o
                        } else {

                            setCustomDialog(rank, homeViewModel.getMyChallengeListToServer(rank.id))

                        }

                    }

                }

            }


        }

    } // setRankingRecyclerViewAndAdapter()


    // 클릭 리스너 초기화
    private fun setListener() {

        // 이번 주 운동량 체크 클릭 리스너
        binding.textViewAllExerciseCheck.setOnClickListener {

            findNavController().navigate(R.id.diaryFragment)

        }

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
        (binding.recyclerViewPagedAllExercise.adapter as PoseExerciseAdapter).exerciseItemClick =
            object: PoseExerciseAdapter.ExerciseItemClick {

            override fun onExerciseItemClick(view: View, position: Int) {

                // 운동이름을 번들로 넘긴다.
                val exerciseName =
                    (binding.recyclerViewPagedAllExercise.adapter as PoseExerciseAdapter).poseExerciseList[position].exerciseName

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

            Log.d(TAG, "setObserve: $it")
            setRankingRecyclerViewAndAdapter()

        }

        homeViewModel.rankingPage.observe(viewLifecycleOwner){
            if(it>1) {
                setRankingRecyclerViewAndAdapter()
            }
        }

    } // setObserve()


    //커스텀 다이얼로그 띄우기
    private fun setCustomDialog(rank: Rank?, userChallengeList: ArrayList<Challenge>?){

        //데이터바인딩 준비
        val inflater = LayoutInflater.from(requireContext())
        customDialogBinding =
            DataBindingUtil.inflate(inflater, R.layout.custom_dialog_challenge_ranking, null, false)

        customDialogBinding.rank = rank

        //다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(customDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        //확인 버튼 클릭하면 다이얼로그 종료
        customDialogBinding.buttonCheck.setOnClickListener {

            dialog.dismiss()

        }

        // 서클 이미지뷰 설정
        Glide.with(this)
            //baseurl+쉐어드의 이미지경로
            .load(homeViewModel.getBaseUrl()+rank?.profileImagePath)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.loading)
            .into(customDialogBinding.circleImageViewUserProfile)

        // 리사이클러뷰 설정
        setAdapter(userChallengeList)

    } // setCustomDialog()


    //커스텀 다이얼로그 띄우기
    private fun setNetworkCustomDialog(){

        // 부모가 있는지 확인하고, 있다면 부모에서 제거
        customNetworkDialogBinding.root.parent?.let {
            (it as ViewGroup).removeView(customNetworkDialogBinding.root)
        }

        //다이얼로그 생성
        val networkDialog = AlertDialog.Builder(requireContext())
            .setView(customNetworkDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        networkDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customNetworkDialogBinding.textViewButtonOk.setOnClickListener {

            networkDialog.dismiss()

        }

        networkDialog.setOnCancelListener {

            networkDialog.dismiss()

        }

        networkDialog.show()

    } // setNetworkCustomDialog()


    // 리사이클러뷰와 어댑터 설정
    private fun setAdapter(userChallengeList: ArrayList<Challenge>?) {

        userChallengeList?.let {

               val challengeJoinAdapter = ChallengeJoinAdapter(it)

                // RecyclerView 설정
                customDialogBinding.recyclerViewUserChallenge.adapter = challengeJoinAdapter

            }

    } // setAdapter()


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

    } // setOnBackPressed()

}