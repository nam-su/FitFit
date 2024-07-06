package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import com.example.fitfit.function.pose.Pose
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

class DiaryModel {

    private val TAG = "다이어리 모델"
    private val myPoseExerciseList = MyApplication.sharedPreferences.getMyAllExerciseList()
    private var entryArrayList = ArrayList<BarEntry>()
    private var allExerciseMap = LinkedHashMap<String,MutableList<Float>>()

    init {
        Log.d(TAG, "${MyApplication.sharedPreferences} ")

        myPoseExerciseList.forEach {
            Log.d(TAG, "[${it.category},${it.exerciseName},${it.exerciseCount},${it.goalExerciseCount},${it.date},${it.checkList}]: ")
        }

//        setLabelMap()
        setAllExerciseMap()
        initEntryArrayList()
        setEntryArrayList(Date(),Date())


    }



    //내 운동 리스트 불러오기
    fun getMyPoseExerciseList(): ArrayList<PoseExercise> { return myPoseExerciseList }



    //그래프에 사용할 entryList 불러오기
    fun getEntryArrayList(startDate: Date?, endDate:Date?): ArrayList<BarEntry> {

        setEntryArrayList(startDate, endDate)

        return entryArrayList

    } //getEntryArrayList()



    //그래프에 사용할 해시맵 불러오기
    fun getAllExerciseMap(): LinkedHashMap<String,MutableList<Float>>{ return allExerciseMap }


//
//    //hashMap 셋팅
//    private fun setLabelMap(){
//
//        labelMap[0f] = "기본 스쿼트"
//        labelMap[1f] = "와이드 스쿼트"
//        labelMap[2f] = "기본 푸시업"
//        labelMap[3f] = "기본 런지"
//        labelMap[4f] = "오른쪽 런지"
//        labelMap[5f] = "왼쪽 런지"
//        labelMap[6f] = "기본 레그레이즈"
//        labelMap[7f] = "오른쪽 레그레이즈"
//        labelMap[8f] = "왼쪽 레그레이즈"
//
//    } //setLabelMap()



    //allExerciseMap 초기화
    private fun setAllExerciseMap(){

        //allExerciseMap 초기값 설정
        //mutableFloatList는 날짜마다 따로 값을 배열에 받기 위해서 사용
        allExerciseMap["기본 스쿼트"] = mutableListOf()
        allExerciseMap["기본 푸시업"] = mutableListOf()
        allExerciseMap["기본 런지"] = mutableListOf()
        allExerciseMap["기본 레그레이즈"] = mutableListOf()
        allExerciseMap["와이드 스쿼트"] = mutableListOf()
        allExerciseMap["오른쪽 런지"] = mutableListOf()
        allExerciseMap["왼쪽 런지"] = mutableListOf()
        allExerciseMap["오른쪽 레그레이즈"] = mutableListOf()
        allExerciseMap["왼쪽 레그레이즈"] = mutableListOf()

    }



    //entryArrayList 셋팅
    private fun initEntryArrayList(){

        //entryArrayList 초기값 설정
        allExerciseMap.values.forEachIndexed { index, floats ->
            entryArrayList.add(BarEntry(index.toFloat(),floats.toFloatArray()))

        }

    }



    //entryArrayList 셋팅
    private fun resetEntryArrayList(){

        reOrderAllExerciseMap()

        allExerciseMap.values.forEachIndexed { index, floats ->
            entryArrayList[index] = BarEntry(index.toFloat(), floats.sum())
        }

    }



    //entryArrayList 셋팅
    private fun setEntryArrayList(startDate: Date?, endDate: Date?){

        setAllExerciseMap()

        myPoseExerciseList.forEach{ poseExercise ->

            // 타임스탬프를 Instant로 변환
            val instant = Instant.ofEpochMilli(poseExercise.date)
            // Instant를 Date로 변환
            val date = Date.from(instant)

            //날짜 안에 있는 데이터만 추가
            if(date.after(setToMidnight(startDate!!)) && date.before(setToEndOfDay(endDate!!))) {

                //날짜에 맞게 데이터 allExerciseMap에 넣기
                allExerciseMap[poseExercise.exerciseName]?.add(poseExercise.exerciseCount.toFloat())

            }

        }

        resetEntryArrayList()


    }



    //0시 00분으로 만들기
    private fun setToMidnight(date: Date): Date {

        val calendar = Calendar.getInstance()
        calendar.time = date

        // 시간을 0시 00분 00초 000밀리초로 설정합니다.
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time

    } //setToMidnight()



    //23시 59분 59초로 만들기
    private fun setToEndOfDay(date: Date): Date {

        val calendar = Calendar.getInstance()
        calendar.time = date

        // 시간을 23시 59분 59초 999밀리초로 설정합니다.
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)

        return calendar.time

    } //setToEndOfDay()


    // allExerciseMap에서 float 배열의 합이 0인 항목들을 맨 뒤로 보내는 메서드
    private fun reOrderAllExerciseMap() {

        val entriesToRemove = mutableListOf<String>()

        // 0인 항목들을 찾아서 entriesToRemove에 추가
        allExerciseMap.forEach { (key, values) ->
            if (values.sum() == 0f) {
                entriesToRemove.add(key)
            }
        }

        // entriesToRemove에 있는 항목들을 순서대로 맨 뒤로 이동
        entriesToRemove.forEach { key ->
            if (allExerciseMap.containsKey(key)) {
                val values = allExerciseMap.remove(key)
                if (values != null) {
                    allExerciseMap[key] = values
                }
            }
        }

    } //reOrderAllExerciseMap()


    // 싱글톤에서 내 도전리스트 받아오기
    fun getMyChallengeList(): ArrayList<Challenge> = MyApplication.sharedPreferences.myChallengeList
     // getMyChallengeList()



    // 이번달 내에 운동리스트로 필터링
    fun getFilteredMyExerciseList(): ArrayList<PoseExercise>{

        val filteredList = myPoseExerciseList.filter {
            it.date > getStartOfMonth() && it.date < getEndOfMonth()
        }

        return ArrayList(filteredList)

    } //getFilteredMyExerciseList()


   


    // 기본부터 챌린지 리스트로 만들기
    fun setSecondChallengeList(){

        getFilteredMyExerciseList().forEach {

        }

    } // setChallengeList()


    //이번달의 1일 0시 00분 0초 구하기
    fun getStartOfMonth(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis

    } //getStartOfMonth()


    //이번달의 마지막날 23시 59분 59초 구하기
    fun getEndOfMonth(): Long {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.DATE, -1)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis

    } // getEndOfMonth()


}