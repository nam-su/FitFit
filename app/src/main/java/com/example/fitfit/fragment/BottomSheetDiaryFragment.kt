package com.example.fitfit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.fitfit.Decorator.DayDecorator
import com.example.fitfit.Decorator.EventDecorator
import com.example.fitfit.Decorator.SaturdayDecorator
import com.example.fitfit.Decorator.SelectedMonthDecorator
import com.example.fitfit.Decorator.SundayDecorator
import com.example.fitfit.Decorator.TodayDecorator
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentBottomSheetDiaryBinding
import com.example.fitfit.viewModel.DiaryViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


class BottomSheetDiaryFragment(private val viewModel: DiaryViewModel, private val mode: Int) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetDiaryBinding
    private val TAG = "바텀시트 다이어리 프래그먼트"

    lateinit var dayDecorator: DayDecorator
    lateinit var todayDecorator: TodayDecorator
    lateinit var sundayDecorator: SundayDecorator
    lateinit var saturdayDecorator: SaturdayDecorator
    lateinit var selectedMonthDecorator: SelectedMonthDecorator
    lateinit var eventDecorator: EventDecorator

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
        
        setVariable()
        setDecorator()
        setCalendarView()
        setListener()

        }
    
    
    
    //초기화
    private fun setVariable(){
        
        //캘린더 뷰 초기화
        dayDecorator = DayDecorator(requireContext())
        todayDecorator = TodayDecorator(requireContext())
        sundayDecorator = SundayDecorator()
        saturdayDecorator = SaturdayDecorator()
        selectedMonthDecorator = SelectedMonthDecorator(requireContext(), CalendarDay.today().month)
        eventDecorator = EventDecorator(viewModel.getMyPoseExerciseList())
        
    } // setVariable()
    
    
    
    //캘린더 뷰 설정
    private fun setCalendarView() {
        
        setDecorator()
        
        binding.calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader)

        binding.calendarView.setTitleFormatter { day ->
            val year = day.year
            val month = day.month + 1
            year.toString() + "년 " + month + "월"
        }

        
    }



    //리스너 설정
    private fun setListener(){

        //월 바꼈을때 리스너
        binding.calendarView.setOnMonthChangedListener { _, date ->
            binding.calendarView.removeDecorators()
            binding. calendarView.invalidateDecorators()
            selectedMonthDecorator = SelectedMonthDecorator(requireContext(), date.month)
            Log.d(TAG, "onMonthChanged: " + date.month)

            setDecorator()
            
        }

        //날짜 변경 리스너
        binding.calendarView.setOnDateChangedListener { _, date, _ ->

            Log.d(TAG, "setListener: ${date.date}")

            binding.buttonSelect.text = viewModel.changeYMDWFormat(date.date)
            binding.buttonSelect.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
            binding.buttonSelect.isEnabled = true

        }

        //선택 버튼 리스너
        binding.buttonSelect.setOnClickListener {

            if(mode == 0){
                viewModel.setStartDate(binding.calendarView.selectedDate.date)
            }else if(mode == 1){
                viewModel.setEndDate(binding.calendarView.selectedDate.date)
            }

            dialog?.dismiss()
        }
    } // setListener()



    //Decorator 추가
    private fun setDecorator(){

        //decorator 추가
        binding.calendarView.addDecorators(dayDecorator, todayDecorator, sundayDecorator,
            saturdayDecorator, selectedMonthDecorator, eventDecorator)

    }
}