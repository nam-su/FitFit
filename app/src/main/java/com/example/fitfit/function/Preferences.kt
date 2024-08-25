package com.example.fitfit.function

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.fitfit.R
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.ExerciseDiary
import com.example.fitfit.data.ExerciseItemInfo
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.User
import com.example.fitfit.function.pose.Pose
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


class Preferences(context: Context) {

    private val preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    private val stringResource = StringResource.PreferencesStringResource

    /** 운동 편집 프래그먼트에서 사용**/
    private val allExerciseList = ArrayList<PoseExercise>()

    /** 다이어리 프래그먼트에서 사용**/
    private var userRecordExerciseList = ArrayList<PoseExercise>()

    /**서버에서 받아온 checkList 저장용**/
    private var userCheckListHashMap = LinkedHashMap<String,Int>()

    private val allExerciseItemInfoList = ArrayList<ExerciseItemInfo>()

    /** 내가 짠 운동 리스트**/
    private lateinit var myExerciseList: ArrayList<PoseExercise>

    /** fitfit 챌린지 리스트**/
    val challengeList = ArrayList<Challenge>()

    /** 이번주 운동 리스트**/
    var myExerciseDiaryList: ArrayList<ExerciseDiary>? = null

    /** 오늘 수행한 운동 리스트**/
    private var todayUserExerciseList: ArrayList<PoseExercise> = ArrayList<PoseExercise>()

    // 현재 제공하는 모든 운동에 관한 내용 더미 데이터
    init {

        setAllExerciseItemInfoList()

    } // init()
    
    /**서버에서 받아온 유저의 checkList를 사용해서 allExerciseList를 초기화 하는 메서드**/
    fun setAllExerciseList(checkList: String) {

        //json string 을 링크드 해시맵에 집어넣는 과정
        val gson = Gson()
        val mapType = object : TypeToken<LinkedHashMap<String, Int>>() {}.type
        userCheckListHashMap = gson.fromJson(checkList, mapType)

        moveZeroValuesToEnd(userCheckListHashMap)

        allExerciseList.clear()

        userCheckListHashMap.forEach { (s,i) ->

            //알맞은 값 어레이리스트에 add
            when {

                s.contains(stringResource.squat) ->
                    allExerciseList.add(PoseExercise(0,stringResource.squat,s,0,10,i))

                s.contains(stringResource.lunge) ->
                    allExerciseList.add(PoseExercise(0,stringResource.lunge,s,0,10,i))

                s.contains(stringResource.pushUp) ->
                    allExerciseList.add(PoseExercise(0,stringResource.pushUp,s,0,10,i))

                s.contains(stringResource.legRaises) ->
                    allExerciseList.add(PoseExercise(0,stringResource.legRaises,s,0,10,i))

            }

        }

        //프리미엄
        allExerciseList.forEach {

            if(it.exerciseName == stringResource.basicSquat ||
                it.exerciseName == stringResource.basicPushUp ||
                it.exerciseName == stringResource.basicLunge) {

                it.isPrimium = 0

            } else {

                it.isPrimium = 1

            }

        }

    } // setAllExerciseList()


    // 내 운동기록 받아온 데이터에서 오늘 운동 데이터만 추출
    fun setTodayMyExerciseDataList() {

            // 오늘 날짜를 구합니다
            val today = LocalDate.now()

            // 사용자 운동 기록 리스트에서 오늘 날짜와 일치하는 운동 데이터만 필터링합니다
            todayUserExerciseList = userRecordExerciseList.filter { poseExercise ->

                // 밀리초 단위의 타임스탬프를 LocalDate로 변환합니다
                val poseExerciseDate = Instant.ofEpochMilli(poseExercise.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                // 날짜를 비교합니다
                poseExerciseDate == today

            } as ArrayList<PoseExercise> // 필터링된 결과를 ArrayList로 변환합니다

            // 오늘 운동 데이터의 exerciseName을 키로 하는 Map을 생성합니다
            val poseExerciseMap = todayUserExerciseList!!.associateBy { it.exerciseName }

            // allExerciseList에서 exerciseName을 키로 하여 poseExercise 객체를 업데이트합니다
            allExerciseList.forEach { allPoseExercise ->

                poseExerciseMap[allPoseExercise.exerciseName]?.let { poseExercise ->

                    // poseExercise의 속성을 allPoseExercise에 복사합니다
                    allPoseExercise.exerciseCount = poseExercise.exerciseCount
                    allPoseExercise.date = poseExercise.date
                  
                }
                
            }

    } // setTodayMyExerciseDataList()


    // 이번주 운동리스트 set해주는 메서드
    fun setExerciseDiaryList(exerciseDiaryList: ArrayList<ExerciseDiary>) {

        myExerciseDiaryList = exerciseDiaryList

    } // setExerciseDiaryList()


    // 이번주 운동리스트 리턴하는 메서드
    fun getExerciseDiaryList() = myExerciseDiaryList
    // getExerciseDiaryList()


    // 이번주 운동리스트 갱신하는 메서드
    fun updateExerciseDiaryList() {

        // 오늘 요일
        // 일 월 화 수 목 금 토
        val today: LocalDate = LocalDate.now()
        val dayOfWeek: DayOfWeek = today.dayOfWeek

        var updateExerciseDiary: ExerciseDiary? = null

        updateExerciseDiary = when (dayOfWeek) {
            DayOfWeek.MONDAY -> ExerciseDiary(day = "월", check = true)
            DayOfWeek.TUESDAY -> ExerciseDiary(day = "화", check = true)
            DayOfWeek.WEDNESDAY -> ExerciseDiary(day = "수", check = true)
            DayOfWeek.THURSDAY -> ExerciseDiary(day = "목", check = true)
            DayOfWeek.FRIDAY -> ExerciseDiary(day = "금", check = true)
            DayOfWeek.SATURDAY -> ExerciseDiary(day = "토", check = true)
            DayOfWeek.SUNDAY -> ExerciseDiary(day = "일", check = true)
        }

        // null 체크
        for (i in myExerciseDiaryList!!.indices) {

            if (myExerciseDiaryList!![i].day == updateExerciseDiary.day) {

                myExerciseDiaryList!![i] = updateExerciseDiary
                break

            }

        }

    } // updateExerciseDiaryList()


    fun exceptionNoDataMyAllExercise(poseExercise: PoseExercise) {

        if (userRecordExerciseList.size == 0) {

            userRecordExerciseList.add(poseExercise)

        }

    } // exceptionNoDataMyAllExercise()


    // 유저 운동기록 리스트 리턴하는 메서드
    fun getUserRecordExerciseList(): ArrayList<PoseExercise> = userRecordExerciseList
    // getUserRecordExerciseList()


    //hashmap value가 0값이 있으면 맨뒤로 보내는 메서드
    private fun moveZeroValuesToEnd(userCheckListHashMap: LinkedHashMap<String, Int>) {

        val zeroKeys = mutableListOf<String>()

        // 값이 0인 키들을 수집
        for ((key, value) in userCheckListHashMap) {

            if (value == 0) {

                zeroKeys.add(key)

            }

        }

        // 값이 0인 요소들을 삭제 후 다시 추가
        for (key in zeroKeys) {

            val value = userCheckListHashMap.remove(key)

            if (value != null) {

                userCheckListHashMap[key] = value

            }

        }

    } // moveZeroValuesToEnd()


    // 운동 이름으로 운동 정보 객체 가져오는 메서드
    fun getExerciseItemInfo(exerciseName: String): ExerciseItemInfo? =
        allExerciseItemInfoList.firstOrNull { it.exerciseName == exerciseName }
     // getExerciseItemInfo()


    // 유저 정보 불러오는 메서드
    fun getUser(): User = User(

            preferences.getString("id", "").toString(),
            preferences.getString("loginType", "").toString(),
            preferences.getString("nickname", "").toString(),
            preferences.getString("profileImagePath", "").toString(),
            preferences.getString("subscription", "").toString())

    // getUser()


    // 유저 아이디만 조회하는 메서드
    fun getUserId(): String = preferences.getString("id", "").toString()
    // getUserId()


    // 유저 닉네임만 조회하는 메서드
    fun getUserNickname(): String = preferences.getString("nickname", "").toString()
     // getUserNickname()


    /**다이어리 프래먼트에서 사용하기 위해 운동전체 리스트 리턴 하는 메서드**/
    fun getMyAllExerciseList(): ArrayList<PoseExercise> = userRecordExerciseList
     // getMyAllExerciseList()


    /**
     * 이것도 다이어리 프래그먼트에서 사용
     * 전체운동 리스트를 서버에서 받아와서 userRecordExerciseList에 추가**/
    private fun setUserAllExerciseList(exerciseList: ArrayList<PoseExercise>) {

        //리스트 비우기
        userRecordExerciseList.clear()
        //리스트 추가
        userRecordExerciseList.addAll(exerciseList)

    } //setUserAllExerciseList()


    /**데이터가 없을 때**/
    fun addRecordExerciseList(poseExercise: PoseExercise) {

        //리스트 추가
        if (!userRecordExerciseList.contains(poseExercise)) {
            userRecordExerciseList.add(poseExercise)
        }

    } //setUserAllExerciseList()


    // 서버에서 유저 운동 정보 불러온 후 리스트에 저장하는 메서드
    /** 내 운동 리스트를 여기서 초기화 함 **/
    fun setUserExerciseInfoList(userAllExerciseList: ArrayList<PoseExercise>) {

        setUserAllExerciseList(userAllExerciseList)

        CoroutineScope(Dispatchers.Main).launch {

            // 시간 복잡도를 O(n * m) 에서 O(n + m) 으로 줄이기 위한 맵 사용
            // 각 운동의 이름을 key 값으로 해서 맵을 만들고 for문 사용
            val exerciseMap = allExerciseList.associateBy { it.exerciseName }.toMutableMap()

            for (exercise in userAllExerciseList) {

                if (exercise.checkList == 1) exerciseMap[exercise.exerciseName] = exercise

            }

            myExerciseList = ArrayList(exerciseMap.values.filter { it.checkList == 1 })

        }

    } // setUserExerciseInfoList()



    // 유저 정보 쉐어드에 저장 하는 메서드 && 유저 체크리스트랑 유저전체 운동리스트 저장
    fun setUserAndAllList(user: User): Boolean {

        setUser(user)

        var checkAllExerciseList = false
        var checkUserAllExerciseList = false
        var checkUserChallengeList = false

        //서버의 체크리스트를 프리퍼런스에서 배열로 갖고 있자.
        if(user.checkList != null) {

            setAllExerciseList(user.checkList!!)

            checkAllExerciseList = true

        }

        //불러온 전체 운동리스트 저장
        if(user.userAllExerciseList != null) {

            setUserAllExerciseList(user.userAllExerciseList!!)

            // 시간 복잡도를 O(n * m) 에서 O(n + m) 으로 줄이기 위한 맵 사용
            // 각 운동의 이름을 key 값으로 해서 맵을 만들고 for문 사용
            val exerciseMap = allExerciseList.associateBy { it.exerciseName }.toMutableMap()

            for (exercise in user.userAllExerciseList!!) {

                if (exercise.checkList == 1) exerciseMap[exercise.exerciseName] = exercise

            }

            myExerciseList = ArrayList(exerciseMap.values.filter { it.checkList == 1 })

            checkUserAllExerciseList = true

        }else{
            setUserAllExerciseList(ArrayList<PoseExercise>())
        }

        //불러온 챌린지 리스트 저장
        if(user.challengeList != null) {
            challengeList.clear()
            challengeList.addAll(user.challengeList!!)
            checkUserChallengeList = true
        }

        return checkAllExerciseList && checkUserAllExerciseList && checkUserChallengeList

    } // setUserAndAllList()


    // 유저 정보 쉐어드에 저장하기
    fun setUser(user: User) {

        editor.putString("id", user.id)
        editor.putString("nickname", user.nickname)
        editor.putString("loginType", user.loginType)
        editor.putString("profileImagePath", user.profileImagePath)
        editor.putString("subscription", user.subscription)
        editor.apply()

    } // setUser()


    // 스케쥴링한 운동 리스트 불러오는 메서드
    fun getMyPoseExerciseList(): ArrayList<PoseExercise> {

        val poseExerciseList = ArrayList<PoseExercise>()

        poseExerciseList.addAll(myExerciseList)

        /**유저의 체크리스트를 통해 현재 운동리스트 편집**/
        poseExerciseList.removeIf { userCheckListHashMap[it.exerciseName] == 0 }

        return poseExerciseList

    } // getMyPoseExerciseList()


    // 스케쥴링한 운동 리스트 저장하는 메서드
    private fun setMyPoseExerciseList(poseExerciseList: ArrayList<PoseExercise>) {

        myExerciseList = poseExerciseList

    } // setExerciseSchedule()


    // 운동 후 리스트 갱신하는 메서드
    fun updateMyPoseExerciseList(poseExercise: PoseExercise) {

        val poseExerciseList = getMyPoseExerciseList()

        val index = poseExerciseList.indexOfFirst { it.exerciseName == poseExercise.exerciseName }

        // 이름이 같은 운동이 있으면 갱신해 준다
        if (index != -1) {

            poseExerciseList[index] = poseExercise

        }

        setMyPoseExerciseList(poseExerciseList)

    } // updatePoseExerciseList()


    // 운동 정보 불러오는 메서드
    fun getPoseExercise(exerciseName: String?): PoseExercise {

        val exerciseMap = myExerciseList.associateBy { it.exerciseName }

        return exerciseMap[exerciseName]!!

    } // loadPoseExercise


    /**로그아웃 또는 회원탈퇴시 쉐어드 갱신과 리스트, 해시맵 비우기**/
    fun removeAll() {

        allExerciseList.clear()
        userRecordExerciseList.clear()
        userCheckListHashMap.clear()
        myExerciseList.clear()
        challengeList.clear()
        myExerciseDiaryList = null
        editor.clear()
        editor.apply()

    } // removeAll()


    // 제공되는 운동 리스트 리턴
    fun getAllExerciseList(): ArrayList<PoseExercise> = allExerciseList
    // getAllExerciseList()


    /**
     * 해시맵 전체리스트를 추가하고 0으로 설정한 뒤
     * 내 편집리스트의 exerciseName과 일치하는 key의 value를 1(true) 로 설정
     * **/
    fun setUserCheckListHashMap(poseExerciseList: ArrayList<PoseExercise>){

        //해시맵 초기화
        userCheckListHashMap.forEach { (key, _) ->

            userCheckListHashMap[key] = 0

        }

        //리스트 값에 따른 해시맵 변경
        poseExerciseList.forEach {

               userCheckListHashMap[it.exerciseName] = 1

        }

    } //setUserCheckListHashMap()


    // 전체 운동 정보 리스트 초기화 하는 메서드
    private fun setAllExerciseItemInfoList() {

        allExerciseItemInfoList.add(ExerciseItemInfo(
            stringResource.basicPushUp,
            R.drawable.push_up_index_0,
            R.drawable.push_up_index_1,
            stringResource.basicPushUpContent0,
            stringResource.basicPushUpContent1,
            false))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            stringResource.basicSquat,
            R.drawable.squat_index_0,
            R.drawable.squat_index_1,
            stringResource.basicSquatContent0,
            stringResource.basicSquatContent1,
            false))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            stringResource.basicLunge,
            R.drawable.lunge_index_0,
            R.drawable.lunge_index_1,
            stringResource.basicLungeContent0,
            stringResource.basicLungeContent1,
            false))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            stringResource.wideSquat,
            R.drawable.wide_squat_index_0,
            R.drawable.wide_squat_index_1,
            stringResource.wideSquatContent0,
            stringResource.wideSquatContent1,
            true))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            stringResource.leftLunge,
            R.drawable.right_lunge_index_0,
            R.drawable.left_lunge_index_1,
            stringResource.leftLungeContent0,
            stringResource.leftLungeContent1,
            true))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            stringResource.rightLunge,
            R.drawable.right_lunge_index_0,
            R.drawable.right_lunge_index_1,
            stringResource.rightLungeContent0,
            stringResource.rightLungeContent1,
            true))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            stringResource.basicLegRaises,
            R.drawable.leg_raises_index_0,
            R.drawable.leg_raises_index_1,
            stringResource.basicLegRaisesContent0,
            stringResource.basicLegRaisesContent1,
            true))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            stringResource.leftLegRaises,
            R.drawable.left_leg_raises_index_0,
            R.drawable.left_leg_raises_index_1,
            stringResource.leftLegRaisesContent0,
            stringResource.leftLegRaisesContent1,
            true))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            stringResource.rightLegRaises,
            R.drawable.right_leg_raises_index_0,
            R.drawable.right_leg_raises_index_1,
            stringResource.rightLegRaisesContent0,
            stringResource.rightLegRaisesContent1,
            true))

    } // setAllExerciseItemInfoList()


    // myExerciseList 초기화
    fun setMyExerciseList(exerciseList: ArrayList<PoseExercise>){

        myExerciseList.clear()

        myExerciseList.addAll(exerciseList)

    } // setMyExerciseList()


    // 홈프래그먼트에서 사용할 기본 운동 리스트
    fun getBasicExerciseList(): ArrayList<PoseExercise> {

        return arrayListOf(

            PoseExercise(0, stringResource.squat, stringResource.basicSquat, 0, 10, 0)
                .apply { isPrimium = 0 },

            PoseExercise(0, stringResource.pushUp, stringResource.basicPushUp, 0, 10, 0)
                .apply { isPrimium = 0 },

            PoseExercise(0, stringResource.lunge, stringResource.basicLunge, 0, 10, 0)
                .apply { isPrimium = 0 },

            PoseExercise(0, stringResource.squat, stringResource.wideSquat, 0, 10, 0)
                .apply { isPrimium = 1 },

            PoseExercise(0, stringResource.lunge, stringResource.leftLunge, 0, 10, 0)
                .apply { isPrimium = 1 },

            PoseExercise(0, stringResource.lunge, stringResource.rightLunge, 0, 10, 0)
                .apply { isPrimium = 1 },

            PoseExercise(0, stringResource.legRaises, stringResource.basicLegRaises, 0, 10, 0)
                .apply { isPrimium = 1 },

            PoseExercise(0, stringResource.legRaises, stringResource.leftLegRaises, 0, 10, 0)
                .apply { isPrimium = 1 },

            PoseExercise(0, stringResource.legRaises, stringResource.rightLegRaises, 0, 10, 0)
                .apply { isPrimium = 1 }
        
        )

    } // getBasicExerciseList()


    //   네트워크 연결 상태를 논리 값으로 반환.
    //   와이파이, 모바일 데이터 연결 중일 경우 true.
    fun getNetworkStatus(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork

        val actNetwork = connectivityManager.getNetworkCapabilities(network)

        return actNetwork?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

    } // getNetworkStatus()

}