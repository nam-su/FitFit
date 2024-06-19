package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import com.github.mikephil.charting.data.BarEntry
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

class DiaryModel {

    private val TAG = "다이어리 모델"
    private val myPoseExerciseList = MyApplication.sharedPreferences.myExerciseList
    private var entryArrayList = ArrayList<BarEntry>()
    private var labelMap = HashMap<Float,String>()
    private var allExerciseMap = HashMap<String,MutableList<Float>>()

    init {

        setLabelMap()
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
    fun getLabelMap(): HashMap<Float,String>{ return labelMap }



    //hashMap 셋팅
    private fun setLabelMap(){

        //allExerciseMap 초기값 설정
        //mutableFloatList는 날짜마다 따로 값을 배열에 받기 위해서 사용
        allExerciseMap["기본 스쿼트"] = mutableListOf(0f)
        allExerciseMap["기본 푸시업"] = mutableListOf(0f)
        allExerciseMap["기본 런지"] = mutableListOf(0f)
        allExerciseMap["와이드 스쿼트"] = mutableListOf(0f)
        allExerciseMap["와이드 푸시업"] = mutableListOf(0f)
        allExerciseMap["와이드 런지"] = mutableListOf(0f)

        labelMap[0f] = "기본 스쿼트"
        labelMap[1f] = "기본 푸시업"
        labelMap[2f] = "기본 런지"
        labelMap[3f] = "와이드 스쿼트"
        labelMap[4f] = "와이드 푸시업"
        labelMap[5f] = "와이드 런지"

    } //setLabelMap()



    //entryArrayList 셋팅
    private fun initEntryArrayList(){
        //entryArrayList 초기값 설정
        for(i in 0 until allExerciseMap.size) {
            Log.d(TAG, "initEntryArrayList: $i")
            entryArrayList.add(BarEntry(i.toFloat(), allExerciseMap[labelMap[i.toFloat()]]?.toFloatArray()))
        }
    }



    //entryArrayList 셋팅
    private fun resetEntryArrayList(){
        //entry 값 재설정 넣기
        for(i in 0 until entryArrayList.size){

            Log.d(TAG, "resetEntryArrayList: $i")
            Log.d(TAG, "resetEntryArrayList: ${labelMap[i.toFloat()]}")
            entryArrayList[i] = BarEntry(i.toFloat(), allExerciseMap[labelMap[i.toFloat()]]!!.sum())

            Log.d(TAG, "resetEntryArrayList: ${allExerciseMap[labelMap[i.toFloat()]]!!.sum()}")
        }
    }



    //entryArrayList 셋팅
    private fun setEntryArrayList(startDate: Date?, endDate: Date?){

        //myPoseExerciseList 에서 더한 값 받아오기.
        myPoseExerciseList.forEach{ poseExercise ->

            // 타임스탬프를 Instant로 변환
            val instant = Instant.ofEpochMilli(poseExercise.date)

            // Instant를 Date로 변환
            val date = Date.from(instant)

            Log.d(TAG, "date: $date")
            Log.d(TAG, "startDate: ${setToMidnight(startDate!!)}")
            Log.d(TAG, "endDate: ${setToEndOfDay(endDate!!)}")

            if(date.after(setToMidnight(startDate!!)) && date.before(setToEndOfDay(endDate!!))) {
                //날짜에 맞게 데이터 allExerciseMap에 넣기
                allExerciseMap[poseExercise.exerciseName]?.add(poseExercise.exerciseCount.toFloat())
                Log.d(TAG, "setEntryArrayList: ${allExerciseMap[poseExercise.exerciseName]}")
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





}