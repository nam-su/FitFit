package com.example.fitfit.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.ExerciseDiary
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.Rank
import com.example.fitfit.model.HomeModel
import kotlinx.coroutines.launch

class HomeViewModel(): ViewModel() {

    val TAG = "홈 뷰모델"

    private val homeModel = HomeModel()

    private val _weekStatus: MutableLiveData<String> = MutableLiveData()
    private val weekStatus: LiveData<String>
        get() = _weekStatus


    // 이번주 운동량에 따른 메시지 출력을 위한 메서드
    fun setWeekStatus(): LiveData<String> {

        _weekStatus.value = homeModel.setWeekStatus() + " 님 이번주 운동은 순항중 이네요."

        return weekStatus

    } // setWeekStatus()


    // 이번주 운동량을 리사이클러뷰에 띄워주는 메서드
    fun setRecyclerViewWeekStatus(): ArrayList<ExerciseDiary>{

        return homeModel.setWeekStatusList()

    } // setRecyclerViewWeekStatus()


    // 랭킹 전체보기 리스트 리사이클러뷰에 띄워주는 메서드
    fun setRecyclerViewAllChallengeRank(): ArrayList<Rank> {

        return homeModel.setAllChallengeRankList()

    } // setRecyclerViewAllChallengeRank()


    // 홈 프래그먼트에서 보이는 랭킹 리사이클러뷰 띄워주는 메서드
    fun setRecyclerViewPagedChallengeRank(): ArrayList<Rank> {

        return homeModel.setPagedChallengeRankList()

    } // setRecyclerViewPagedChallengeRank()


    // 운동 전체보기 리사이클러뷰 띄워주는 메서드
    fun setRecyclerViewAllExercise(): ArrayList<PoseExercise> {

        return homeModel.setAllExerciseList()

    } // setRecyclerViewAllExercise()

}