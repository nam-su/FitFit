package com.example.fitfit.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.model.DiaryModel
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryViewModel: ViewModel() {

    private val diaryModel = DiaryModel()

    private var _startDate = MutableLiveData<Date>(Date())
    val startDate: LiveData<Date>
        get() = _startDate

    private var _endDate = MutableLiveData<Date>(Date())
    val endDate: LiveData<Date>
        get() = _endDate


    // 시작날짜 선택
    fun setStartDate(date: Date){ _startDate.value = date }
    // setStartDate()


    // 끝날짜 선택
    fun setEndDate(date: Date){ _endDate.value = date }
    // setEndDate()


    //date를 년월일요일 형태로 바꿔주는 메서드
    fun changeYMDWFormat(date: Date): String{

        // 원하는 출력 형식
        val format = SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN)

        try {

            // Date 객체를 원하는 형식으로 변환
            return format.format(date)

        } catch (e: ParseException) {

            e.printStackTrace()

        }

        return ""

    } // changeYMDWFormat()


    //date를 년월일 형태로 바꿔주는 메서드
    fun changeYMDFormat(date: Date): String{

        // 원하는 출력 형식
        val format = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN)

        try {

            // Date 객체를 원하는 형식으로 변환
            return format.format(date)

        } catch (e: ParseException) {

            e.printStackTrace()

        }

        return ""

    } //changeYMDFormat()


    //운동 리스트 불러오기
    fun getMyPoseExerciseList(): ArrayList<PoseExercise> = diaryModel.getMyPoseExerciseList()
    // getMyPoseExerciseList()


    //모델에서 EntryList 불러오기
    fun getEntryArrayList(): ArrayList<BarEntry> = diaryModel.getEntryArrayList(startDate.value, endDate.value)
    // getEntryArrayList()


    // 제일 큰 값 계산
    fun calculateMaxY(): Float{

            var maxYValue = Float.MIN_VALUE

            for (entry in diaryModel.getEntryArrayList(startDate.value, endDate.value)) {

                if (entry.y > maxYValue) {

                    maxYValue = entry.y

                }

            }

            return maxYValue

    } //calculateMaxY()


    //labelMap 불러오기
    fun getAllExerciseMap(): LinkedHashMap<String,MutableList<Float>> = diaryModel.getAllExerciseMap()
    // getAllExerciseMap()


    // 서버에서 내 도전리스트 호출
    suspend fun getMyChallengeListToServer(): ArrayList<Challenge>? {

        return withContext(Dispatchers.IO) {

            val response = diaryModel.getMyChallengeListToServer()

            if (response.isSuccessful && response.body() != null) {

                response.body()

            } else {

                null

            }

        }

    } // getMyChallengeListToServer()


}