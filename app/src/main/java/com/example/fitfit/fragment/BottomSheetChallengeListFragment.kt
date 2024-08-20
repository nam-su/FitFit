package com.example.fitfit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fitfit.R
import com.example.fitfit.adapter.ChallengeListAdapter
import com.example.fitfit.data.Challenge
import com.example.fitfit.databinding.FragmentBottomSheetChallengeListBinding
import com.example.fitfit.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetChallengeListFragment(private val homeViewModel: HomeViewModel) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetChallengeListBinding
    lateinit var challengeListAdapter: ChallengeListAdapter


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_challenge_list, container, false)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 팝업 생성 시 전체화면으로 띄우기
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)

        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        // 드래그해도 팝업이 종료되지 않도록
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {

                    behavior.state = BottomSheetBehavior.STATE_EXPANDED

                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        setVariable()
        setListener()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        challengeListAdapter = ChallengeListAdapter(homeViewModel.getChallengeListToModel())

        binding.recyclerViewChallengeList.adapter = challengeListAdapter

    } // setVariable()


    // 리스너 초기화
    private fun setListener() {

        // 운동 선택 아이템 클릭 리스너
        challengeListAdapter.challengeItemClick = object : ChallengeListAdapter.ChallengeItemClick {

            override fun onClick(view: View, challenge: Challenge) {

                homeViewModel.setChallengeName(challenge.challengeName)
                dismiss()

            }

        }

    } // setListener()

}