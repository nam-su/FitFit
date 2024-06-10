package com.example.fitfit.model

import com.example.fitfit.function.MyApplication
import com.example.fitfit.data.ExerciseDiary
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.Rank
class HomeModel() {

    // 홈에서 이번주 운동 상태 관련 메시지 정보
    fun setWeekStatus(): String {

        return MyApplication.sharedPreferences.getUserNickname()

    } // setWeekStatus()


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


    fun setPagedChallengeRankList(): ArrayList<Rank> {

        val pagedChallengeRankList = ArrayList<Rank>()

        pagedChallengeRankList.add(Rank(1,"언더테이커",""))
        pagedChallengeRankList.add(Rank(2,"케인",""))
        pagedChallengeRankList.add(Rank(3,"유형선",""))

        return pagedChallengeRankList
    }


    // 다양한 운동 리스트 리턴하는 메서드
    fun setAllExerciseList(): ArrayList<PoseExercise> {

        return MyApplication.sharedPreferences.getAllExerciseList()

    } // setAllExerciseList()

}