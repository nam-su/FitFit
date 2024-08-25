package com.example.fitfit.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.ExerciseDiary
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.Rank
import com.example.fitfit.model.HomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel: ViewModel() {

    private val homeModel = HomeModel()

    private val _weekStatus: MutableLiveData<String> = MutableLiveData()
    val weekStatus: LiveData<String>
        get() = _weekStatus

    private val _challengeName= MutableLiveData<String>(homeModel.getChallengeListToShared()[0].challengeName)
    val challengeName: LiveData<String>
        get() = _challengeName


    private val _ranking= MutableLiveData<String>()
    val ranking: LiveData<String>
        get() = _ranking

    private val _rankingPage= MutableLiveData(1)
    val rankingPage: LiveData<Int>
        get() = _rankingPage


    private val _userRankText = MutableLiveData<String>()
    val userRankText: LiveData<String>
        get() = _userRankText


    // 챌린지 이름 설정
    fun setChallengeName(name: String){ _challengeName.value = name }
    // setChallengeName()


    // 이번주 운동량에 따른 메시지 출력을 위한 메서드
    fun setWeekStatus() {

        val exerciseCompleteDayCount = homeModel.exerciseDiaryList.count { it.check }

        var advise = homeModel.setWeekStatus()

        advise += when(exerciseCompleteDayCount) {

            0 -> { homeModel.stringResource.exercise0 }

            in 1..3 -> { homeModel.stringResource.exercise1 }

            in 4..5 -> { homeModel.stringResource.exercise2 }

            else -> { homeModel.stringResource.exercise3 }

        }

        _weekStatus.value = advise

    } // setWeekStatus()


    // 이번주 운동량을 리사이클러뷰에 띄워주는 메서드
    fun setRecyclerViewWeekStatus(): ArrayList<ExerciseDiary> {

        return homeModel.setWeekStatusList()

    } // setRecyclerViewWeekStatus()



    // 홈 프래그먼트에서 보이는 랭킹 리사이클러뷰 띄워주는 메서드
    suspend fun getRankingListToServer(): ArrayList<Rank> {

        val response = homeModel.getRankingListToServer(getUserId(),challengeName.value,rankingPage.value)

        if(response.isSuccessful && response.body() != null){

            return splitRankingList(response.body()!!)
        }

        return ArrayList()

    } // setRecyclerViewPagedChallengeRank()


    /****/
    // 랭킹페이지에 따른 랭킹리스트와 내 랭킹 나누기
    private fun splitRankingList(rankingList: ArrayList<Rank>): ArrayList<Rank> {

        if (rankingList.isNotEmpty()) {

            return if(rankingList[rankingList.size-1].id == getUserId()) {

                if(rankingList[rankingList.size-1].id == getUserId()) {
<<<<<<< HEAD

                    setUserRank(rankingList[rankingList.size-1])

                } else {

                    _userRankText.value = "챌린지에 참여하고 순위를 경쟁해 보세요."

                }

                ArrayList(rankingList.subList(0,rankingList.size-1))

            } else {

                _userRankText.value = "챌린지에 참여하고 순위를 경쟁해 보세요."
                ArrayList(rankingList.subList(0,rankingList.size))

=======
                    setUserRank(rankingList[rankingList.size-1])
                }else{
                    _userRankText.value = homeModel.stringResource.joinChallenge
                }
                return ArrayList(rankingList.subList(0,rankingList.size-1))

            }else{
                _userRankText.value = homeModel.stringResource.joinChallenge
                return ArrayList(rankingList.subList(0,rankingList.size))
>>>>>>> feature/Refactor
            }

            // 나머지 요소들을 rankingArrayList에 설정

<<<<<<< HEAD
        } else {

            _userRankText.value = "챌린지에 참여하고 순위를 경쟁해 보세요."

=======
        }else{
            _userRankText.value = homeModel.stringResource.joinChallenge
>>>>>>> feature/Refactor
        }

        return arrayListOf()

    } //splitRankingList()


    // My Text 설정
    private fun setUserRank(rank: Rank){

//        _userRankText.value = "${rank.nickname} 님의 현재 순위는 ${rank.ranking} 위 입니다."
        _userRankText.value = String.format(homeModel.stringResource.USER_RANK_TEXT,rank.nickname,rank.ranking)

    } //setUserRank()


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

            if (response.isSuccessful && response.body() != null) {

                response.body()

            } else {

                null

            }

        }

    } // getMyChallengeListToServer()


    //다음순위보기 클릭 시 랭킹페이지 +1
    fun addRankingPage(){
        _rankingPage.value = _rankingPage.value!! + 1
    } //addRankingPage()

}