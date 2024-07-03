package com.example.fitfit.function

import android.content.Context
import android.util.Log
import com.example.fitfit.R
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.ExerciseItemInfo
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.User
import com.example.fitfit.function.pose.Pose
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Preferences(context: Context) {

    private val TAG = "쉐어드"

    private val preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private val editor = preferences.edit()

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
    var challengeList = ArrayList<Challenge>()

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

                s.contains("스쿼트") -> allExerciseList.add(PoseExercise(0,"스쿼트",s,0,0,i))
                s.contains("런지") -> allExerciseList.add(PoseExercise(0,"런지",s,0,0,i))
                s.contains("푸시업") -> allExerciseList.add(PoseExercise(0,"푸시업",s,0,0,i))
                s.contains("레그레이즈") -> allExerciseList.add(PoseExercise(0,"레그레이즈",s,0,0,i))

            }

        }

        //프리미엄
        allExerciseList.forEach {

            if(it.exerciseName == "기본 스쿼트" || it.exerciseName == "기본 푸시업" || it.exerciseName == "기본 런지") {
                it.isPrimium = 0
            }else{
                it.isPrimium = 1
            }

        }


    } // setAllExerciseList()


    // 유저 운동기록 리스트 리턴하는 메서드
    fun getUserRecordExerciseList(): ArrayList<PoseExercise> {

        return userRecordExerciseList

    } // getUserRecordExerciseList()


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

    } //moveZeroValuesToEnd()


    // 운동 이름으로 운동 정보 객체 가져오는 메서드
    fun getExerciseItemInfo(exerciseName: String): ExerciseItemInfo? {

        return allExerciseItemInfoList.firstOrNull { it.exerciseName == exerciseName }

    } // getExerciseItemInfo()


    // 유저 정보 불러오는 메서드
    fun getUser(): User = User(
            preferences.getString("id", "").toString(),
            preferences.getString("loginType", "").toString(),
            preferences.getString("nickname", "").toString(),
            preferences.getString("profileImagePath", "").toString(),
            preferences.getString("subscription", "").toString()
        )
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
    fun setUser(user: User) {

        editor.putString("id", user.id)
        editor.putString("nickname", user.nickname)
        editor.putString("loginType", user.loginType)
        editor.putString("profileImagePath", user.profileImagePath)
        editor.putString("subscription", user.subscription)
        editor.apply()

        //서버의 체크리스트를 프리퍼런스에서 배열로 갖고 있자.
        setAllExerciseList(user.checkList!!)

        //불러온 전체 운동리스트 저장
        setUserRecordExerciseList(user.userAllExerciseList!!)

    } // setUser()


    // 스케쥴링한 운동 리스트 불러오는 메서드
    fun getMyPoseExerciseList(): ArrayList<PoseExercise> {

        val poseExerciseList = ArrayList<PoseExercise>()

        poseExerciseList.addAll(myExerciseList)

        /**유저의 체크리스트를 통해 현재 운동리스트 편집**/
        poseExerciseList.removeIf { userCheckListHashMap[it.exerciseName] == 0 }

        return poseExerciseList

    } // getExerciseSchedule()


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
        editor.clear()
        editor.apply()

    } //removeAll()

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


    //서버의 전체운동리스트 클라이언트에 저장
    private fun setUserRecordExerciseList(userRecordList: ArrayList<PoseExercise>) {

        userRecordExerciseList = userRecordList

    } //setUserRecordExerciseList()


    // 전체 운동 정보 리스트 초기화 하는 메서드
    private fun setAllExerciseItemInfoList() {

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "기본 푸시업",
            R.drawable.push_up_index_0,
            R.drawable.push_up_index_1,
            "팔을 어깨너비보다 약간 넓게 벌린 상태로 엎드린다.",
            "가슴이 바닥에 거의 닿을 때까지 구부리면서 몸을 내린다.",
            false))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "기본 스쿼트",
            R.drawable.squat_index_0,
            R.drawable.squat_index_1,
            "양발의 간격은 어깨넓이로 벌리고 발을 11자로 만들어 선다.",
            "뒤에 의자가 있다고 생각하고 엉덩이를 뒤로 빼면서 앉는다. 이때 무릎이 발가락보다 너무 많이 앞으로 나가면 안된다.",
            false))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "기본 런지",
            R.drawable.lunge_index_0,
            R.drawable.lunge_index_1,
            "두 발을 골반너비로 벌린 자세로 바로 선다.",
            "등과 허리를 바로 편 상태에서 양 무릎을 90도 굽힌다 이때 뒤쪽 무릎이 바닥에 닿는 느낌으로 몸을 내린다.",
            false))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "와이드 스쿼트",
            R.drawable.wide_squat_index_0,
            R.drawable.wide_squat_index_1,
            "다리를 어깨너비보다 넓게 벌리고 양쪽 발끝은 45도 각도로 밖으로 향한다.",
            "무릎이 엄지발가락으로 향하도록 호흡을 들이 마시면서 천천히 무릎을 굽힌다.",
            true))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "왼쪽 런지",
            R.drawable.right_lunge_index_0,
            R.drawable.left_lunge_index_1,
            "두 발을 골반너비로 벌린 자세로 바로 선다.",
            "손은 허리에 놓고 몸통을 한쪽으로 치우치면서 왼쪽 다리를 굽혀준다.반대편 다리는 쭉뻗어준다.",
            true))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "오른쪽 런지",
            R.drawable.right_lunge_index_0,
            R.drawable.right_lunge_index_1,
            "두 발을 골반너비로 벌린 자세로 바로 선다.",
            "손은 허리에 놓고 몸통을 한쪽으로 치우치면서 오른쪽 다리를 굽혀준다.반대편 다리는 쭉뻗어준다.",
            true))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "기본 레그레이즈",
            R.drawable.leg_raises_index_0,
            R.drawable.leg_raises_index_1,
            "손바닥을 지면에 대고 바른 자세로 눕는다.",
            "무릎을 곧게 뻗은 상태에서 복부쪽으로 그대로 다리를 당기고 다리가 지면과 수직을 이루는 지점에서 시작자세로 돌아간다.",
            true))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "왼쪽 레그레이즈",
            R.drawable.left_leg_raises_index_0,
            R.drawable.left_leg_raises_index_1,
            "손에 머리를 기대고 시선을 옆방향에 두고 누워준다.",
            "오른쪽 다리는 그대로 두고 왼쪽 다리를 위로 뻗는다 이때 무릅을 굽히지 않고 통채로 뻗어주면 된다.",
            true))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "오른쪽 레그레이즈",
            R.drawable.right_leg_raises_index_0,
            R.drawable.right_leg_raises_index_1,
            "손에 머리를 기대고 시선을 옆방향에 두고 누워준다.",
            "왼쪽 다리는 그대로 두고 오른쪽 다리를 위로 뻗는다 이때 무릅을 굽히지 않고 통채로 뻗어주면 된다.",
            true))

    } // setAllExerciseItemInfoList()


    //myExerciseList 셋
    fun setMyExerciseList(exerciseList: ArrayList<PoseExercise>){
        myExerciseList.clear()
        myExerciseList.addAll(exerciseList)
    }


}