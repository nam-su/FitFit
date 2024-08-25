package com.example.fitfit.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.fitfit.decorator.AfterTodayDecorator
import com.example.fitfit.decorator.DayDecorator
import com.example.fitfit.decorator.EndDayDecorator
import com.example.fitfit.decorator.EventDecorator
import com.example.fitfit.decorator.SaturdayDecorator
import com.example.fitfit.decorator.SelectedMonthDecorator
import com.example.fitfit.decorator.StartDayDecorator
import com.example.fitfit.decorator.SundayDecorator
import com.example.fitfit.decorator.TodayDecorator
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentBottomSheetDiaryBinding
import com.example.fitfit.viewModel.DiaryViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.prolificinteractive.materialcalendarview.CalendarDay


class BottomSheetDiaryFragment(private val viewModel: DiaryViewModel, private val mode: Int) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetDiaryBinding

    private var onDismissListener: (() -> Unit)? = null

    lateinit var dayDecorator: DayDecorator
    lateinit var todayDecorator: TodayDecorator
    lateinit var sundayDecorator: SundayDecorator
    lateinit var saturdayDecorator: SaturdayDecorator
    lateinit var selectedMonthDecorator: SelectedMonthDecorator
    lateinit var eventDecorator: EventDecorator
    lateinit var afterTodayDecorator: AfterTodayDecorator

    var startDayDecorator = StartDayDecorator(viewModel.startDate.value!!)
    var endDayDecorator = EndDayDecorator(viewModel.endDate.value!!)


    /****/
    fun setOnDismissListener(listener: () -> Unit) {
        onDismissListener = listener
    }


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_diary, container, false)

        return binding.root

    } // onCreateView()


    // onViewCreated()
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

    } // onViewCreated()

    /****/
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }


    // 변수 초기화
    private fun setVariable() {

        //캘린더 뷰 초기화
        dayDecorator = DayDecorator(requireContext())
        todayDecorator = TodayDecorator(requireContext())
        sundayDecorator = SundayDecorator()
        saturdayDecorator = SaturdayDecorator()
        selectedMonthDecorator = SelectedMonthDecorator(requireContext(), CalendarDay.today().month)
        eventDecorator = EventDecorator(viewModel.getMyPoseExerciseList())
        afterTodayDecorator = AfterTodayDecorator()

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

    } // setCalendarView()


    //리스너 설정
    private fun setListener() {

        // 월 바꼈을때 리스너
        binding.calendarView.setOnMonthChangedListener { _, date ->

            binding.calendarView.removeDecorators()
            binding.calendarView.invalidateDecorators()
            selectedMonthDecorator = SelectedMonthDecorator(requireContext(), date.month)

            setDecorator()

        }

        // 날짜 변경 리스너
        binding.calendarView.setOnDateChangedListener { _, date, _ ->

            binding.buttonSelect.text = viewModel.changeYMDWFormat(date.date)
            binding.buttonSelect.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.personal)
            binding.buttonSelect.isEnabled = true

        }

        //선택 버튼 리스너
        binding.buttonSelect.setOnClickListener {

            /****/
            if (mode == 0) {

                viewModel.setStartDate(binding.calendarView.selectedDate.date)

                /****/
            } else if (mode == 1) {

                viewModel.setEndDate(binding.calendarView.selectedDate.date)

            }

            dialog?.dismiss()

        }

    } // setListener()


    //Decorator 추가
    private fun setDecorator() {

        //decorator 추가
        binding.calendarView.addDecorators(
            dayDecorator, todayDecorator,
            sundayDecorator, saturdayDecorator,
            selectedMonthDecorator, eventDecorator)


        // 선택날짜에 따른 decorator 추가
        when (mode) {
            0 -> {

                // mode = 0 => 시작날짜 선택
                // 시작날짜는 마지막날짜보다 이후 날짜를 선택할 수 없음.
                binding.calendarView.addDecorator(endDayDecorator)

            }

            1 -> {

                // mode = 1 => 마지막날짜 선택
                // 마지막 날짜는 시작 날짜보다 이전 날짜를 선택할 수 없음.
                binding.calendarView.addDecorator(startDayDecorator)

            }

        }

        // 마지막에 오늘날짜 이후날짜들 화이트 처리
        binding.calendarView.addDecorator(afterTodayDecorator)

    } // setDecorator()

}