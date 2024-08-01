package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import com.github.mikephil.charting.data.BarEntry
import retrofit2.Response
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class DiaryModel {

    private val TAG = "다이어리 모델"

    private val myPoseExerciseList = MyApplication.sharedPreferences.getMyAllExerciseList()
    private var entryArrayList = ArrayList<BarEntry>()
    private var allExerciseMap = LinkedHashMap<String,MutableList<Float>>()
    private val myChallengeList = ArrayList<Challenge>()

    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface: RetrofitInterface


    init {

        Log.d(TAG, "${MyApplication.sharedPreferences} ")

        myPoseExerciseList.forEach {

            Log.d(TAG, "[${it.category},${it.exerciseName},${it.exerciseCount},${it.goalExerciseCount},${it.date},${it.checkList}]: ")

        }

        setAllExerciseMap()
        initEntryArrayList()
        setEntryArrayList(setToMidnight(Date()),setToEndOfDay(Date()))

    }


    //내 운동 리스트 불러오기
    fun getMyPoseExerciseList(): ArrayList<PoseExercise> = myPoseExerciseList
    // getMyPoseExerciseList()


    //그래프에 사용할 entryList 불러오기
    fun getEntryArrayList(startDate: Date?, endDate:Date?): ArrayList<BarEntry> {

        setEntryArrayList(startDate, endDate)

        return entryArrayList

    } // getEntryArrayList()


    //그래프에 사용할 해시맵 불러오기
    fun getAllExerciseMap(): LinkedHashMap<String,MutableList<Float>> = allExerciseMap
    // getAllExerciseMap()


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

    } // setAllExerciseMap()


    //entryArrayList 셋팅
    private fun initEntryArrayList(){

        //entryArrayList 초기값 설정
        allExerciseMap.values.forEachIndexed { index, floats ->

            entryArrayList.add(BarEntry(index.toFloat(),floats.toFloatArray()))

        }

    } // initEntryArrayList()


    //entryArrayList 셋팅
    private fun resetEntryArrayList(){

        reOrderAllExerciseMap()

        allExerciseMap.values.forEachIndexed { index, floats ->

            entryArrayList[index] = BarEntry(index.toFloat(), floats.sum())

        }

    } // resetEntryArrayList()


    //entryArrayList 셋팅
    private fun setEntryArrayList(startDate: Date?, endDate: Date?){

        setAllExerciseMap()

        myPoseExerciseList.forEach{ poseExercise ->

            // 타임스탬프를 Instant로 변환
            val instant = Instant.ofEpochMilli(poseExercise.date)

            // Instant를 Date로 변환
            val date = setToNoon(Date.from(instant))
            Log.d(TAG, "setToMidnight: ${setToMidnight(startDate!!)}")
            Log.d(TAG, "date: $date")
            Log.d(TAG, "setToEndOfDay: ${setToEndOfDay(endDate!!)}")

            //날짜 안에 있는 데이터만 추가
            if(date.after(setToMidnight(startDate!!)) && date.before(setToEndOfDay(endDate!!))) {

                //날짜에 맞게 데이터 allExerciseMap에 넣기
                allExerciseMap[poseExercise.exerciseName]?.add(poseExercise.exerciseCount.toFloat())

            }

        }

        resetEntryArrayList()

    } // setEntryArrayList()


    // 0시 00분으로 만들기
    private fun setToMidnight(date: Date): Date {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.time = date

        // 시간을 0시 00분 00초 000밀리초로 설정합니다.
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    } // setToMidnight()


    // 12시 00분으로 만들기 (정오)
    private fun setToNoon(date: Date): Date {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.time = date

        // 시간을 12시 00분 00초 000밀리초로 설정합니다.
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    } // setToNoon()

    // 23시 59분 59초로 만들기
    private fun setToEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.time = date

        // 시간을 23시 59분 59초 999밀리초로 설정합니다.
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)

        return calendar.time
    } // setToEndOfDay()

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

    } // reOrderAllExerciseMap()


    //서버에서 받아온 챌린지 리스트를 싱글톤에 저장하는 메서드
    suspend fun getMyChallengeListToServer(): Response<ArrayList<Challenge>> {

        retrofitBuilder = RetrofitBuilder()
        retrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

        return retrofitInterface.getMyChallengeList(MyApplication.sharedPreferences.getUserId(),"getMyChallengeList")

    } // saveMyChallengeList()


    //서버에서 받아온 챌린지 리스트를 싱글톤에 저장하는 메서드
    fun getMyChallengeList(): ArrayList<Challenge> = myChallengeList
    // saveMyChallengeList()


    // 챌린지 리스트 초기화
    fun setMyChallengeList(list: ArrayList<Challenge>){

        myChallengeList.clear()
        myChallengeList.addAll(list)

    } //setMyChallengeList()


}