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
import com.example.fitfit.Decorator.DayDecorator
import com.example.fitfit.Decorator.EventDecorator
import com.example.fitfit.Decorator.SaturdayDecorator
import com.example.fitfit.Decorator.SelectedMonthDecorator
import com.example.fitfit.Decorator.SundayDecorator
import com.example.fitfit.Decorator.TodayDecorator
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentDiaryBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.prolificinteractive.materialcalendarview.CalendarDay


class DiaryFragment : Fragment() {

    private val TAG = "다이어리 프래그먼트"

    lateinit var binding: FragmentDiaryBinding
    lateinit var labelMap: HashMap<Float,String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_diary,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()

        setBarChart(binding.barChart)

    }



    //리스너 설정
    private fun setListener(){

        //시작날짜 선택 리스너
        binding.buttonStartDate.setOnClickListener {
            val bottomSheetDiaryFragment = BottomSheetDiaryFragment()
            bottomSheetDiaryFragment.show(parentFragmentManager,"")
        }

        //마지막날짜 선택 리스너
        binding.buttonEndDate.setOnClickListener {
            val bottomSheetDiaryFragment = BottomSheetDiaryFragment()
            bottomSheetDiaryFragment.show(parentFragmentManager,"")
        }

    } //setListener()



    //barchart 데이터 셋팅
    private fun setBarChart(barChart: BarChart) {

        initBarChart(barChart)

        barChart.setScaleEnabled(false) //Zoom In/Out

        val valueList = ArrayList<Double>()
        val entries: ArrayList<BarEntry> = ArrayList()
        labelMap = HashMap<Float,String>()
        val title = "내 운동"

        //input data
        for (i in 1..5) {
            valueList.add(i * 100.1)
        }

        //fit the data into a bar
        // BarEntry 추가 및 labelMap에 문자열 추가

        entries.add(BarEntry(1f, 10f))
        labelMap[1f] = "스쿼트"
        entries.add(BarEntry(2f, 20f))
        labelMap[2f] = "푸시업"
        entries.add(BarEntry(3f, 30f))
        labelMap[3f] = "런지"

        val barDataSet = BarDataSet(entries, title)

        barDataSet.apply {
            val colorList = listOf(
                ContextCompat.getColor(requireContext(), R.color.squat),
                ContextCompat.getColor(requireContext(), R.color.pushUp),
                ContextCompat.getColor(requireContext(), R.color.lunge)
            )
            colors = colorList
            //Setting the size of the form in the legend
            formSize = 15f
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
                    return labelMap[value] // x 값에 해당하는 문자열 반환
                }
            }

        }


        //barChart의 좌측 좌표값 설정
       barChart.axisLeft.apply {
           setDrawGridLines(false)
           setDrawAxisLine(false)
           isEnabled = false
           setDrawLabels(false)
       }


        //barChart의 우측값 설정
        barChart.axisRight.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isEnabled = false
            setDrawLabels(false)
        }

        //바차트의 타이틀
        barChart.legend.apply {
            //setting the shape of the legend form to line, default square shape
            form = Legend.LegendForm.LINE
            //setting the text size of the legend
            textSize = 11f
            textColor = Color.BLACK
            typeface = Typeface.DEFAULT_BOLD
            //setting the alignment of legend toward the chart
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            //setting the stacking direction of legend
            orientation = Legend.LegendOrientation.HORIZONTAL
            //setting the location of legend outside the chart, default false if not set
            setDrawInside(false)
        }

    } //initBarChart()

}