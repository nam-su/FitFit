package com.example.fitfit.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentDiaryBinding
import com.example.fitfit.function.MyApplication
import com.example.fitfit.viewModel.DiaryViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date


class DiaryFragment : Fragment() {

    private val TAG = "다이어리 프래그먼트"

    lateinit var binding: FragmentDiaryBinding

    lateinit var diaryViewModel: DiaryViewModel

    lateinit var barDataSet: BarDataSet


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_diary,container,false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()

        setListener()

        setObserve()

        setBarChart(binding.barChart)

    }



    //초기값 설정
    private fun setVariable(){

        binding.lifecycleOwner = this

        diaryViewModel = DiaryViewModel()

        binding.diaryViewModel = diaryViewModel

    } // setVariable()



    //리스너 설정
    private fun setListener(){

        //시작날짜 선택 리스너
        binding.buttonStartDate.setOnClickListener {

            val bottomSheetDiaryFragment = BottomSheetDiaryFragment(diaryViewModel,0)
            bottomSheetDiaryFragment.show(parentFragmentManager,"")

        }

        //마지막날짜 선택 리스너
        binding.buttonEndDate.setOnClickListener {

            val bottomSheetDiaryFragment = BottomSheetDiaryFragment(diaryViewModel,1)
            bottomSheetDiaryFragment.show(parentFragmentManager,"")

        }

    } //setListener()


    //observe 설정
    private fun setObserve(){

        //첫번째 선택 날짜 관찰
        diaryViewModel.startDate.observe(viewLifecycleOwner){

            binding.buttonStartDate.text = diaryViewModel.changeYMDFormat(it)
            setBarChart(binding.barChart)

        }

        //두번째 선택 날짜 관찰
        diaryViewModel.endDate.observe(viewLifecycleOwner){

            binding.buttonEndDate.text = diaryViewModel.changeYMDFormat(it)
            setBarChart(binding.barChart)

        }
    }



    //barchart 데이터 셋팅
    private fun setBarChart(barChart: BarChart) {

        initBarChart(barChart)

        barDataSet = BarDataSet(diaryViewModel.getEntryArrayList(), "")



            barDataSet.apply {

                val colorList = listOf(
                    ContextCompat.getColor(requireContext(), R.color.squat),
                    ContextCompat.getColor(requireContext(), R.color.pushUp),
                    ContextCompat.getColor(requireContext(), R.color.lunge)
                )

                colors = colorList
                //Setting the size of the form in the legend
                formSize = 15f
                //막대 너비 설정
                //showing the value of the bar, default true if not set
                setDrawValues(false)
                //setting the text size of the value of the bar
                valueTextSize = 12f

            }

            val data = BarData(barDataSet)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            barChart.data = data
            barChart.invalidate()

    } // setBarChart()



    //BarChart 초기 설정
    private fun initBarChart(barChart: BarChart) {

        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false)
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false)
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false)

        barChart.setTouchEnabled(false)   // 터치 이벤트 비활성화
        barChart.setPinchZoom(false)     // 핀치 줌 비활성화
        barChart.setScaleEnabled(false)  // 스케일링 비활성화

        //remove the description label text located at the lower right corner
        val description = Description()
        description.isEnabled = false
        barChart.description = description

        //X, Y 바의 애니메이션 효과
        barChart.animateY(1000)
        barChart.animateX(1000)


        //바텀 좌표 값 설정
        barChart.xAxis.apply {

            //hiding the x-axis line, default true if not set
            setDrawAxisLine(false)

            //hiding the vertical grid lines, default true if not set
            setDrawGridLines(false)

            //change the position of x-axis to the bottom
            position = XAxis.XAxisPosition.BOTTOM

            //set the horizontal distance of the grid line
            granularity = 1f

            typeface = Typeface.DEFAULT_BOLD


            //x축 값에 문자열 넣는 부분 (원래 Float 형태만 출력됐음)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String? {
                    return diaryViewModel.getLabelMap()[value] // x 값에 해당하는 문자열 반환
                }
            }

        }


        //barChart의 좌측 좌표값 설정
         barChart.axisLeft.apply {

            axisMinimum = 0f  // 최소 값 설정
            axisMaximum = diaryViewModel.calculateMaxY()
            setDrawGridLines(false)
            setDrawAxisLine(true)
            isEnabled = true
            setDrawLabels(true)

         }

        //barChart의 우측값 설정
         barChart.axisRight.apply {

            setDrawGridLines(false)
            setDrawAxisLine(false)
            isEnabled = false
            setDrawLabels(true)

            }

        //바차트의 타이틀(범례) 설정
        barChart.legend.isEnabled = false

    } //initBarChart()

}
