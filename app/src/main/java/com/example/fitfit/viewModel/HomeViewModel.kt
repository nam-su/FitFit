package com.example.fitfit.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitfit.model.HomeModel

class HomeViewModel(application: Application): AndroidViewModel(application) {

    val TAG = "홈 뷰모델"

    private val homeModel = HomeModel(application.applicationContext)

    private val weekStatus: MutableLiveData<String> = MutableLiveData()


    // 이번주 운동량에 따른 메시지 출력을 위한 메서드
    fun setWeekStatus(): LiveData<String> {

        weekStatus.value = homeModel.setWeekStatus() + " 님 이번주 운동은 순항중 이네요."

        return weekStatus

    } // setWeekStatus()

}