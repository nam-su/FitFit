package com.example.fitfit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitfit.model.DiaryModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryViewModel {

    private val TAG = "다이어리 뷰모델"

    private val diaryModel = DiaryModel()

    private var _startDate = MutableLiveData<Date>()
    val startDate: LiveData<Date>
        get() = _startDate

    private var _endDate = MutableLiveData<Date>()
    val endDate: LiveData<Date>
        get() = _endDate


    fun setStartDate(date: Date){ _startDate.value = date }

    fun setEndDate(date: Date){ _endDate.value = date }


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
    }



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
    }

}