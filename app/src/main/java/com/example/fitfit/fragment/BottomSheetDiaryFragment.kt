package com.example.fitfit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fitfit.Decorator.DayDecorator
import com.example.fitfit.Decorator.EventDecorator
import com.example.fitfit.Decorator.SaturdayDecorator
import com.example.fitfit.Decorator.SelectedMonthDecorator
import com.example.fitfit.Decorator.SundayDecorator
import com.example.fitfit.Decorator.TodayDecorator
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentBottomSheetDiaryBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.prolificinteractive.materialcalendarview.CalendarDay


class BottomSheetDiaryFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetDiaryBinding
    private val TAG = "바텀시트 다이어리 프래그먼트"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bottom_sheet_diary,container,false)

        return binding.root
    }

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

        setCalendarView()
        setListener()

        }


    private fun setCalendarView() {

        //캘린더 뷰 셋팅
        var dayDecorator = DayDecorator(requireContext())
        var todayDecorator = TodayDecorator(requireContext())
        var sundayDecorator = SundayDecorator()
        var saturdayDecorator = SaturdayDecorator()
        var selectedMonthDecorator = SelectedMonthDecorator(requireContext(), CalendarDay.today().month)
        var eventDecorator = EventDecorator(arrayListOf())

        binding.calendarView.addDecorators(
            dayDecorator,
            todayDecorator,
            sundayDecorator,
            saturdayDecorator,
            selectedMonthDecorator,
            eventDecorator
        )
        binding.calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader)
        binding.calendarView.setTitleFormatter { day ->
            val year = day.year
            val month = day.month + 1
            year.toString() + "년 " + month + "월"
        }
        binding.calendarView.setOnMonthChangedListener { _, date ->
            binding.calendarView.removeDecorators()
            binding. calendarView.invalidateDecorators()
            selectedMonthDecorator = SelectedMonthDecorator(requireContext(), date.month)
            Log.d(TAG, "onMonthChanged: " + date.month)
            binding.calendarView.addDecorators(
                dayDecorator,
                todayDecorator,
                sundayDecorator,
                saturdayDecorator,
                selectedMonthDecorator,
                eventDecorator
            )
        }


        //제목 눌렸을 때!
        binding.calendarView.setOnTitleClickListener { Log.d(TAG, "onClick: 제목 눌렸엉") }


        //날짜 바꿨을 때!
        binding.calendarView.setOnDateChangedListener { _, _, _ ->

        }
    }



    //리스너 설정
    private fun setListener(){
        binding.buttonSelect.setOnClickListener {
            dialog?.dismiss()
        }
    } // setListener()
}