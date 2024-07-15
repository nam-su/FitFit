package com.example.fitfit.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.ExerciseDiary
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.Rank
import com.example.fitfit.model.HomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel: ViewModel() {

    val TAG = "홈 뷰모델"

    private val homeModel = HomeModel()

    private val _weekStatus: MutableLiveData<String> = MutableLiveData()
    val weekStatus: LiveData<String>
        get() = _weekStatus

    private val _challengeName= MutableLiveData(homeModel.getChallengeListToShared()[0].challengeName)
    val challengeName: LiveData<String>
        get() = _challengeName

    private val _userNickname= MutableLiveData<String>()
    val userNickname: LiveData<String>
        get() = _userNickname

    private val _ranking= MutableLiveData<String>()
    val ranking: LiveData<String>
        get() = _ranking


    // 챌린지 이름 설정
    fun setChallengeName(name: String){ _challengeName.value = name }
    // setChallengeName()


    // 이번주 운동량에 따른 메시지 출력을 위한 메서드
    fun setWeekStatus(): String = homeModel.setWeekStatus() + " 님 이번주 운동은 순항중 이네요."
    // setWeekStatus()


    // 이번주 운동량을 리사이클러뷰에 띄워주는 메서드
    fun setRecyclerViewWeekStatus(): ArrayList<ExerciseDiary> = homeModel.setWeekStatusList()
    // setRecyclerViewWeekStatus()


    // 홈 프래그먼트에서 보이는 랭킹 리사이클러뷰 띄워주는 메서드
    suspend fun getRankingListToServer(): ArrayList<Rank> {

        val response = homeModel.getRankingListToServer(challengeName.value)

        Log.d(TAG, "getRankingListToServer: ${response.isSuccessful}")
        Log.d(TAG, "getRankingListToServer: ${response.body()}")

        if(response.isSuccessful && response.body() != null){

            response.body()?.forEach {

                if(it.id == getUserId()){

                    _userNickname.value = it.nickname
                    _ranking.value = it.ranking.toString()
                }

            }

            return response.body()!!
        }

        return ArrayList()

    } // setRecyclerViewPagedChallengeRank()


    // 운동 전체보기 리사이클러뷰 띄워주는 메서드
    fun getBasicExerciseList(): ArrayList<PoseExercise> = homeModel.getBasicExerciseList()
    // setRecyclerViewAllExercise()


    // 모델에서 fitfit의 챌린지 리스트 요청
    fun getChallengeListToModel(): ArrayList<Challenge> = homeModel.getChallengeListToShared()
    // getChallengeListToModel()


    //이미지 baseUrl 받아오기
    fun getBaseUrl(): String? = homeModel.getBaseUrl()
    //getBaseUrl()


    // 유저 id 받아오기
    fun getUserId(): String = homeModel.getUserId()
    // getUserId()


    // 서버에서 내 도전리스트 호출
    suspend fun getMyChallengeListToServer(userId: String): ArrayList<Challenge>? {

        return withContext(Dispatchers.IO) {

            val response = homeModel.getMyChallengeListToServer(userId)

            Log.d(TAG, "getMyChallengeList: ${response.isSuccessful}")
            Log.d(TAG, "getMyChallengeList: ${response.body()?.size}")

            if (response.isSuccessful && response.body() != null) {

                response.body()

            } else {

                null

            }

        }

    } // getMyChallengeListToServer()


}