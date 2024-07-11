package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.Challenge
import com.example.fitfit.function.MyApplication
import com.example.fitfit.data.ExerciseDiary
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.Rank
import com.example.fitfit.function.pose.Pose
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeModel() {

    val TAG = "홈 모델"

    // 오늘 날짜 기준 일주일 날짜 리스트
    var userRecordExerciseList: ArrayList<PoseExercise>? = null

    private val exerciseDiaryList = ArrayList<ExerciseDiary>()

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    // 홈에서 이번주 운동 상태 관련 메시지 정보
    fun setWeekStatus(): String {

//        setWeek()

        return MyApplication.sharedPreferences.getUserNickname()

    } // setWeekStatus()

    
    // 유저 운동 정보 리스트 리턴하는 메서드
    fun setWeekStatusList(): ArrayList<ExerciseDiary>{

        setWeek()

        return exerciseDiaryList

    } // setWeekStatusList()


    // 홈프래그먼트에 보여지는 랭킹 리스트 서버에서 불러오기
    suspend fun getRankingListToServer(challengeName: String?): Response<ArrayList<Rank>> = retrofitInterface.getRankingList(challengeName, "getRankingList")
    // setPagedChallengeRankList()


    // 다양한 운동 리스트 리턴하는 메서드
    fun setAllExerciseList(): ArrayList<PoseExercise> {

        return MyApplication.sharedPreferences.getAllExerciseList()

    } // setAllExerciseList()


    // 오늘 날짜로 부터 일주일 날짜 구하는 메서드 (일요일 부터 토요일 까지)
    private fun setWeek() {

        userRecordExerciseList = MyApplication.sharedPreferences.getUserRecordExerciseList()

        // 날짜 형식 지정
        val datePattern = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

        // 현재 날짜를 기준으로 주차 정의
        val day: String = datePattern.format(Date())
        val dateArray = day.split("-").toTypedArray()
        val cal = Calendar.getInstance()
        cal.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, dateArray[2].toInt())
        cal.firstDayOfWeek = Calendar.SUNDAY

        // 이번 주의 첫 번째 일요일로 이동
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        // 이번 주 날짜 리스트 초기화
        val weekDateList = ArrayList<String>()
        val weekDays = listOf("일", "월", "화", "수", "목", "금", "토")

        for (i in 0 until 7) {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(cal.time)
            weekDateList.add(date)

            // 기록 여부 확인
            val hasRecord = userRecordExerciseList!!.any {
                SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date(it.date)) == date
            }
            Log.d(TAG, "setWeek: Date=$date, hasRecord=$hasRecord")
            exerciseDiaryList.add(ExerciseDiary(weekDays[i], hasRecord))

            cal.add(Calendar.DATE, 1)
        }

    } // setWeek()


    //fitfit 챌린지 리스트 받아오기
    fun getChallengeListToShared(): ArrayList<Challenge> = MyApplication.sharedPreferences.challengeList
    // getChallengeListToShared()


    //싱글톤 객체에서 baseUrl 받아오기
    fun getBaseUrl(): String? = retrofitBuilder.baseUrl
    // getBaseUrl


    // 유저 아이디 정보 받아오기
    fun getUserId(): String = MyApplication.sharedPreferences.getUserId()
    // getUserId()


    //서버에서 받아온 챌린지 리스트를 싱글톤에 저장하는 메서드
    suspend fun getMyChallengeListToServer(userId: String): Response<ArrayList<Challenge>> = retrofitInterface.getMyChallengeList(userId,"getMyChallengeList")
    // saveMyChallengeList()
}