package com.example.fitfit.model

import com.example.fitfit.function.MyApplication
import com.example.fitfit.data.ExerciseDiary
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.Rank
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeModel() {

    // 오늘 날짜 기준 일주일 날짜 리스트
    private val weekDateList = ArrayList<String>()

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    // 홈에서 이번주 운동 상태 관련 메시지 정보
    fun setWeekStatus(): String {

        setWeek()
        return MyApplication.sharedPreferences.getUserNickname()

    } // setWeekStatus()


    // 서버에 유저 아이디로 운동정보 요청하는 메서드
    suspend fun selectUserExercise(): Response<ArrayList<PoseExercise>> {

        val id = MyApplication.sharedPreferences.getUserId()
        val mode = "selectUserExerciseFromId"

        return retrofitInterface.selectUserExercise(id,mode)

    } // selectUserExercise()


    // 유저 운동 정보 리스트 리턴하는 메서드
    fun setWeekStatusList(): ArrayList<ExerciseDiary>{

        val exerciseDiaryList = ArrayList<ExerciseDiary>()

        exerciseDiaryList.add(ExerciseDiary("일","true"))
        exerciseDiaryList.add(ExerciseDiary("월","true"))
        exerciseDiaryList.add(ExerciseDiary("화","true"))
        exerciseDiaryList.add(ExerciseDiary("수","false"))
        exerciseDiaryList.add(ExerciseDiary("목","false"))
        exerciseDiaryList.add(ExerciseDiary("금","false"))
        exerciseDiaryList.add(ExerciseDiary("토","false"))

        return exerciseDiaryList

    } // setWeekStatusList()


    // 챌린지 랭킹 정보 리스트 리턴하는 메서드
    fun setAllChallengeRankList(): ArrayList<Rank> {

        val challengeRankList = ArrayList<Rank>()

        challengeRankList.add(Rank(1,"언더테이커",""))
        challengeRankList.add(Rank(2,"케인",""))
        challengeRankList.add(Rank(3,"유형선",""))
        challengeRankList.add(Rank(4,"유형선",""))
        challengeRankList.add(Rank(5,"유형선",""))
        challengeRankList.add(Rank(6,"유형선",""))
        challengeRankList.add(Rank(7,"유형선",""))
        challengeRankList.add(Rank(8,"유형선",""))
        challengeRankList.add(Rank(9,"유형선",""))
        challengeRankList.add(Rank(10,"유형선",""))

        return challengeRankList

    } // setChallengeRankList()


    // 홈프래그먼트에 보여지는 3명 랭킹 리스트
    fun setPagedChallengeRankList(): ArrayList<Rank> {

        val pagedChallengeRankList = ArrayList<Rank>()

        pagedChallengeRankList.add(Rank(1,"언더테이커",""))
        pagedChallengeRankList.add(Rank(2,"케인",""))
        pagedChallengeRankList.add(Rank(3,"유형선",""))

        return pagedChallengeRankList

    } // setPagedChallengeRankList()


    // 다양한 운동 리스트 리턴하는 메서드
    fun setAllExerciseList(): ArrayList<PoseExercise> {

        return MyApplication.sharedPreferences.getAllExerciseList()

    } // setAllExerciseList()


    // 오늘 날짜로 부터 일주일 날짜 구하는 메서드 (일요일 부터 토요일 까지)
    private fun setWeek() {

        val datePattern = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

        // format(시스템시간) 입력하면 그 날에 주차를 정의한다.
        val day: String = datePattern.format(Date())

        val dateArray = day.split("-").toTypedArray()

        val cal = Calendar.getInstance()
        cal[dateArray[0].toInt(), dateArray[1].toInt() - 1] = dateArray[2].toInt()

        cal.firstDayOfWeek = Calendar.SUNDAY

        // 시작일과 특정날짜의 차이를 구한다
        val dayOfWeek = cal[Calendar.DAY_OF_WEEK] - cal.firstDayOfWeek

        // 해당 주차의 첫째날을 지정한다
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek)

        // 해당 주차의 첫째 날짜 (일요일)
        val sunday = datePattern.format(cal.time)
        weekDateList.add(sunday)

        // 해당 주차의 둘째 날짜 (월요일)
        cal.add(Calendar.DATE, 1)
        val monday = datePattern.format(cal.time)
        weekDateList.add(monday)

        // 해당 주차의 셋째 날짜 (화요일)
        cal.add(Calendar.DATE, 1)
        val tuesday = datePattern.format(cal.time)
        weekDateList.add(tuesday)

        // 해당 주차의 넷째 날짜 (수요일)
        cal.add(Calendar.DATE, 1)
        val wednesday = datePattern.format(cal.time)
        weekDateList.add(wednesday)

        // 해당 주차의 다섯째 날짜 (목요일)
        cal.add(Calendar.DATE, 1)
        val thursday = datePattern.format(cal.time)
        weekDateList.add(thursday)

        // 해당 주차의 여섯째 날짜 (금요일)
        cal.add(Calendar.DATE, 1)
        val friday = datePattern.format(cal.time)
        weekDateList.add(friday)

        // 해당 주차의 일곱째 날짜 (토요일)
        cal.add(Calendar.DATE, 1)
        val saturday = datePattern.format(cal.time)
        weekDateList.add(saturday)

    } // setWeek()

}