package com.example.fitfit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.fitfit.Decorator.AfterTodayDecorator
import com.example.fitfit.Decorator.DayDecorator
import com.example.fitfit.Decorator.EndDayDecorator
import com.example.fitfit.Decorator.EventDecorator
import com.example.fitfit.Decorator.SaturdayDecorator
import com.example.fitfit.Decorator.SelectedMonthDecorator
import com.example.fitfit.Decorator.StartDayDecorator
import com.example.fitfit.Decorator.SundayDecorator
import com.example.fitfit.Decorator.TodayDecorator
import com.example.fitfit.R
import com.example.fitfit.adapter.ChallengeListAdapter
import com.example.fitfit.adapter.ExerciseChoiceAdapter
import com.example.fitfit.data.Challenge
import com.example.fitfit.databinding.FragmentBottomSheetChallengeListBinding
import com.example.fitfit.databinding.FragmentBottomSheetDiaryBinding
import com.example.fitfit.viewModel.DiaryViewModel
import com.example.fitfit.viewModel.ExerciseChoiceViewModel
import com.example.fitfit.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class BottomSheetChallengeListFragment(private val homeViewModel: HomeViewModel) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetChallengeListBinding
    lateinit var challengeListAdapter: ChallengeListAdapter

    private val TAG = "바텀시트 챌린지 리스트 프래그먼트"


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