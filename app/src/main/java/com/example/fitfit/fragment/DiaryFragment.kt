package com.example.fitfit.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.adapter.ChallengeJoinAdapter
import com.example.fitfit.databinding.CustomDialogNetworkDisconnectBinding
import com.example.fitfit.databinding.FragmentDiaryBinding
import com.example.fitfit.function.MyApplication
import com.example.fitfit.viewModel.DiaryViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch


class DiaryFragment : Fragment() {

    lateinit var binding: FragmentDiaryBinding
    lateinit var diaryViewModel: DiaryViewModel
    private var challengeJoinAdapter: ChallengeJoinAdapter? = null

    lateinit var customDialogBinding: CustomDialogNetworkDisconnectBinding

    private lateinit var callback: OnBackPressedCallback


    // onAttach()
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach


    // onCreateView()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_diary,container,false)
        customDialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_network_disconnect, null, false)

        return binding.root

    } // onCreateView()


    // onViewCreated()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setListener()
        setObserve()
        setBarChart(binding.barChart)
        setAdapter()

    } // onViewCreated()


    //초기값 설정
    private fun setVariable(){

        (activity as MainActivity).visibleBottomNavi()

        binding.lifecycleOwner = this

        diaryViewModel = DiaryViewModel()

        binding.diaryViewModel = diaryViewModel

    } // setVariable()


    // 리사이클러뷰와 어댑터 설정
    private fun setAdapter() {

        if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

            setCustomDialog()

        } else {

            // 네트워크 요청을 수행하고 어댑터를 설정
            lifecycleScope.launch {

                val challengeList = diaryViewModel.getMyChallengeListToServer()

                challengeList?.let {

                    challengeJoinAdapter = ChallengeJoinAdapter(it)

                    // RecyclerView 설정
                    binding.recyclerView.adapter = challengeJoinAdapter

                    if(challengeJoinAdapter?.itemCount!! < 1) {

                        binding.textViewNonChallenge.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE

                    } else {

                        binding.textViewNonChallenge.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE

                    }

                }

            }

        }

    } // setAdapter()


    // 리스너 설정
    private fun setListener(){

        // 시작 날짜 선택 리스너
        binding.buttonStartDate.setOnClickListener {

            // 버튼을 클릭한 후 비활성화
            binding.buttonStartDate.isEnabled = false
            binding.buttonEndDate.isEnabled = false

            val bottomSheetDiaryFragment = BottomSheetDiaryFragment(diaryViewModel, 0)
            bottomSheetDiaryFragment.show(parentFragmentManager, "")

            // BottomSheet가 닫힌 후 다시 활성화
            bottomSheetDiaryFragment.setOnDismissListener {

                binding.buttonStartDate.isEnabled = true
                binding.buttonEndDate.isEnabled = true

            }

        }

        // 마지막 날짜 선택 리스너
        binding.buttonEndDate.setOnClickListener {

            // 버튼을 클릭한 후 비활성화
            binding.buttonStartDate.isEnabled = false
            binding.buttonEndDate.isEnabled = false

            val bottomSheetDiaryFragment = BottomSheetDiaryFragment(diaryViewModel, 1)
            bottomSheetDiaryFragment.show(parentFragmentManager, "")

            // BottomSheet가 닫힌 후 다시 활성화
            bottomSheetDiaryFragment.setOnDismissListener {

                binding.buttonStartDate.isEnabled = true
                binding.buttonEndDate.isEnabled = true

            }

        }

    } //setListener()


    // observe 설정
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

    } // setObserve()


    // barchart 데이터 셋팅
    private fun setBarChart(barChart: BarChart) {

        initBarChart(barChart)

        val filteredEntryList = diaryViewModel.getEntryArrayList().filter { it.y != 0f }

        //필터링안 데이터 비어있을때 안비어있을 때 처리
        if(filteredEntryList.isEmpty()) {

            binding.barChart.visibility = View.GONE
            binding.textViewEmpty.visibility = View.VISIBLE

        } else {

            binding.barChart.visibility = View.VISIBLE
            binding.textViewEmpty.visibility = View.GONE

        }

        val keysList = diaryViewModel.getAllExerciseMap().keys.toMutableList()

        val labels = ArrayList<String>()
        labels.addAll(keysList)

        val barDataSet = BarDataSet(filteredEntryList, "")

        val colorList = listOf(
            ContextCompat.getColor(requireContext(), R.color.squat),
            ContextCompat.getColor(requireContext(), R.color.pushUp),
            ContextCompat.getColor(requireContext(), R.color.lunge),
            ContextCompat.getColor(requireContext(), R.color.legRaises)
        )

            barDataSet.apply {

                colors = colorList
                //Setting the size of the form in the legend
                formSize = 15f
                //막대 너비 설정
                //showing the value of the bar, default true if not set
                setDrawValues(false)
                //setting the text size of the value of the bar
                valueTextSize = 12f

            }

            val barData = BarData(barDataSet)
            barData.setValueTypeface(Typeface.DEFAULT_BOLD)
            barData.barWidth = 0.4f
            barChart.data = barData
            barChart.invalidate()
            barChart.setFitBars(true) //바를 차트에 맞춤

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
        barChart.animateY(500)
        barChart.animateX(500)

        //바텀 좌표 값 설정
        barChart.xAxis.apply {

            //hiding the x-axis line, default true if not set
            setDrawAxisLine(true)
            setDrawGridLines(false)  //x축의 그리드 라인 숨기기

            granularity = 1f

            textSize = 6f

            //change the position of x-axis to the bottom
            position = XAxis.XAxisPosition.BOTTOM

            granularity = 1f // x축의 최소 간격을 1로 설정

            typeface = Typeface.DEFAULT_BOLD

            //x축 값에 문자열 넣는 부분 (원래 Float 형태만 출력됐음)
            valueFormatter = object : ValueFormatter() {

                override fun getFormattedValue(value: Float): String {

                    val keyList = diaryViewModel.getAllExerciseMap().keys.toList()

                    return keyList[value.toInt()] // x 값에 해당하는 문자열 반환

                }

            }

        }

        // barChart의 좌측 좌표값 설정
        barChart.axisLeft.apply {

            // y축 간격을 1로 설정 (즉, 눈금 간격이 1)
            granularity = 1f

            // y축 최소값을 0으로 설정
            axisMinimum = 0f

            // y축 최대값을 설정 (diaryViewModel의 calculateMaxY() 함수에 의해 계산된 값)
            axisMaximum = diaryViewModel.calculateMaxY()

            // y축의 그리드 라인을 그리지 않도록 설정
            setDrawGridLines(false)

            // y축의 축선을 그리도록 설정
            setDrawAxisLine(true)

            // y축을 활성화
            isEnabled = true

            // y축 레이블을 그리도록 설정
            setDrawLabels(true)

            // y축 값들을 정수로 표현하도록 설정
            valueFormatter = object : ValueFormatter() {

                override fun getFormattedValue(value: Float): String {

                    // 값 포맷팅: 소수점 없이 정수로 변환하여 반환
                    return value.toInt().toString()

                }

            }

        }

        //barChart의 우측값 설정
         barChart.axisRight.apply {

            setDrawGridLines(false)
            setDrawAxisLine(false)
            isEnabled = false
            setDrawLabels(true)

            }


        barChart.legend.apply {

            isEnabled = false

        }

    } // initBarChart()


    //커스텀 다이얼로그 띄우기
    private fun setCustomDialog(){

        // 부모가 있는지 확인하고, 있다면 부모에서 제거
        customDialogBinding.root.parent?.let {
            (it as ViewGroup).removeView(customDialogBinding.root)
        }

        //다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(customDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customDialogBinding.textViewButtonOk.setOnClickListener {

            dialog.dismiss()

        }

        dialog.show()

    } // setCustomDialog()


    // 뒤로가기 눌렀을때
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                (activity as MainActivity).finish()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    } // onBackPressed()

}
