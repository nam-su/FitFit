package com.example.fitfit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.fitfit.Decorator.DayDecorator
import com.example.fitfit.Decorator.EventDecorator
import com.example.fitfit.Decorator.SaturdayDecorator
import com.example.fitfit.Decorator.SelectedMonthDecorator
import com.example.fitfit.Decorator.SundayDecorator
import com.example.fitfit.Decorator.TodayDecorator
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentDiaryBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class DiaryFragment : Fragment() {

    private val TAG = "다이어리 프래그먼트"

    lateinit var binding: FragmentDiaryBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_diary,container,false)

        setCalendarView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setCalendarView() {
        //캘린더뷰 셋팅
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


        //제목눌렸을때!
        binding.calendarView.setOnTitleClickListener { Log.d(TAG, "onClick: 제목 눌렸엉") }


        //날짜 바꿨을때!
        binding.calendarView.setOnDateChangedListener { _, _, _ ->

        }
    }



    //리스너 세팅


}