package com.example.fitfit.function

import android.content.Context
import android.util.Log
import com.example.fitfit.R
import com.example.fitfit.data.ExerciseInfo
import com.example.fitfit.data.ExerciseItemInfo
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.User
import com.example.fitfit.function.pose.Pose
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class Preferences(context: Context) {

    private val TAG = "쉐어드"

    private val preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    /** 운동 편집 프래그먼트에서 사용**/
    private val allExerciseList = ArrayList<PoseExercise>()

    /** 다이어리 프래그먼트에서 사용**/
    private val userRecordExerciseList = ArrayList<PoseExercise>()

    /**서버에서 받아온 checkList 저장용**/
    private val userCheckListHashMap = HashMap<String,Int>()

    private val allExerciseItemInfoList = ArrayList<ExerciseItemInfo>()

    /** 내가 짠 운동 리스트**/
    private var myExerciseList = ArrayList<PoseExercise>()

    // 현재 제공하는 모든 운동에 관한 내용 더미 데이터
    init {

        setAllExerciseItemInfoList()

    }

    /**서버에서 받아온 유저의 checkList를 사용해서 allExerciseList를 초기화 하는 메서드**/
    fun setAllExerciseList(hashMap: HashMap<String,Int>) {

        //한번 비우고
        allExerciseList.clear()

        //해시맵 돌려서
        hashMap.forEach { (s, i) ->

            // 서버테이블 컬럼에는 띄어쓰기가 안되서 클라이언트에서 변환
            var newValue = s.replace("_"," ")
            userCheckListHashMap[s.replace("_"," ")] = i

            //알맞은 값 어레이리스트에 add
            when {
                newValue.contains("스쿼트") -> allExerciseList.add(PoseExercise(0,"스쿼트",newValue,0,0,i))
                newValue.contains("런지") -> allExerciseList.add(PoseExercise(0,"런지",newValue,0,0,i))
                newValue.contains("푸시업") -> allExerciseList.add(PoseExercise(0,"푸시업",newValue,0,0,i))
                newValue.contains("레그레이즈") -> allExerciseList.add(PoseExercise(0,"레그레이즈",newValue,0,0,i))
            }
            Log.d(TAG, "setAllExerciseList: ${allExerciseList.size}")
        }

        //프리미엄 딱지 추가
        for (i: Int in 3 until allExerciseList.size) {

            allExerciseList[i].isPrimium = 1

        }


    } // setAllExerciseList()


    // 전체 운동 정보 리스트 초기화 하는 메서드
    private fun setAllExerciseItemInfoList() {

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "기본 푸시업",
            R.drawable.push_up_index_0,
            R.drawable.push_up_index_1,
            "팔을 어깨너비보다 약간 넓게 벌린 상태로 엎드린다.",
            "가슴이 바닥에 거의 닿을 때까지 구부리면서 몸을 내린다."))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "기본 스쿼트",
            R.drawable.squat_index_0,
            R.drawable.squat_index_1,
            "양발의 간격은 어깨넓이로 벌리고 발을 11자로 만들어 선다.",
            "뒤에 의자가 있다고 생각하고 엉덩이를 뒤로 빼면서 앉는다. 이때 무릎이 발가락보다 너무 많이 앞으로 나가면 안된다."))

        allExerciseItemInfoList.add(ExerciseItemInfo(
            "기본 런지",
            R.drawable.lunge_index_0,
            R.drawable.lunge_index_1,
            "두 발을 골반너비로 벌린 자세로 바로 선다.",
            "등과 허리를 바로 편 상태에서 양 무릎을 90도 굽힌다 이때 뒤쪽 무릎이 바닥에 닿는 느낌으로 몸을 내린다."))

    } // setAllExerciseItemInfoList()


    // 운동 이름으로 운동 정보 객체 가져오는 메서드
    fun getExerciseItemInfo(exerciseName: String): ExerciseItemInfo? {

        var getExerciseItemInfo: ExerciseItemInfo? = null

        for (exerciseItemInfo in allExerciseItemInfoList) {

            if(exerciseItemInfo.exerciseName == exerciseName) {

                getExerciseItemInfo = exerciseItemInfo

            }

        }

        return getExerciseItemInfo

    } // getExerciseItemInfo()


    // 유저 정보 불러오는 메서드
    fun getUser(): User {

        preferences.getString("id", "")
        preferences.getString("nickname", "")
        preferences.getString("loginType", "")
        preferences.getString("profileImagePath", "")
        preferences.getString("subscription", "")

        return User(
            preferences.getString("id", "").toString(),
            preferences.getString("loginType", "").toString(),
            preferences.getString("nickname", "").toString(),
            preferences.getString("profileImagePath", "").toString(),
            preferences.getString("subscription", "").toString()
        )

    } // getUser()


    // 유저 아이디만 조회하는 메서드
    fun getUserId(): String {

        return preferences.getString("id", "").toString()

    } // getUserId()


    // 유저 닉네임만 조회하는 메서드
    fun getUserNickname(): String {

        return preferences.getString("nickname", "").toString()

    } // getUserNickname()


    /**다이어리 프래먼트에서 사용하기 위해 운동전체 리스트 리턴 하는 메서드**/
    fun getMyAllExerciseList(): ArrayList<PoseExercise> {

        userRecordExerciseList.forEach {
            Log.d(TAG, "get : [${it.category},${it.exerciseName},${it.exerciseCount},${it.goalExerciseCount},${it.date},${it.checkList}]: ")
        }

        return userRecordExerciseList

    } // getMyAllExerciseList()


    /**
     * 이것도 다이어리 프래그먼트에서 사용
     * 전체운동 리스트를 서버에서 받아와서 userRecordExerciseList에 추가**/
    private fun setUserAllExerciseList(exerciseList: ArrayList<PoseExercise>) {
        Log.d(TAG, "setMyExerciseList 호출")

        userRecordExerciseList.clear()
        userRecordExerciseList.addAll(exerciseList)

        userRecordExerciseList.forEach {
            Log.d(
                TAG,
                "[set : ${it.category},${it.exerciseName},${it.exerciseCount},${it.goalExerciseCount},${it.date},${it.checkList}]: "
            )
        }

    } // setUserAllExerciseList()


    // 서버에서 유저 운동 정보 불러온 후 리스트에 저장하는 메서드
    /** 내 운동 리스트를 여기서 초기화 함 **/
    fun setUserExerciseInfoList(userAllExerciseList: ArrayList<PoseExercise>) {

        setUserAllExerciseList(userAllExerciseList)

        CoroutineScope(Dispatchers.Main).launch {

            // 시간 복잡도를 O(n * m) 에서 O(n + m) 으로 줄이기 위한 맵 사용
            // 각 운동의 이름을 key 값으로 해서 맵을 만들고 for문 사용
            val exerciseMap = allExerciseList.associateBy { it.exerciseName }.toMutableMap()

            for (exercise in userAllExerciseList) {

                if (exercise.checkList == 1) {

                    exerciseMap[exercise.exerciseName] = exercise

                }

            }

            myExerciseList = ArrayList(exerciseMap.values.filter { it.checkList == 1 })

        }

    } // setUserExerciseInfoList()


    // 유저 정보 쉐어드에 저장 하는 메서드
    fun setUser(user: User) {

        editor.putString("id", user.id)
        editor.putString("nickname", user.nickname)
        editor.putString("loginType", user.loginType)
        editor.putString("profileImagePath", user.profileImagePath)
        editor.putString("subscription", user.subscription)
        editor.apply()

        //서버의 체크리스트 저장
        setAllExerciseList(user.checkList!!)

    } // setUser()


    // 스케쥴링한 운동 리스트 불러오는 메서드
    fun getMyPoseExerciseList(): ArrayList<PoseExercise> {

        val poseExerciseList = ArrayList<PoseExercise>()

        poseExerciseList.addAll(myExerciseList)

        /**유저의 체크리스트를 통해 현재 운동리스트 편집**/
        poseExerciseList.removeIf {

            if (userCheckListHashMap[it.exerciseName] == 0) {

                Log.d(TAG, "getMyPoseExerciseList: ${it.exerciseName},${it.checkList}")
                true // 조건에 맞는 항목을 삭제

            } else {

                false

            }

        }

        return poseExerciseList

    } // getExerciseSchedule()


    // 스케쥴링한 운동 리스트 저장하는 메서드
    fun setMyPoseExerciseList(poseExerciseList: ArrayList<PoseExercise>) {

        myExerciseList = poseExerciseList

    } // setExerciseSchedule()


    // 운동 후 리스트 갱신하는 메서드
    fun updateMyPoseExerciseList(poseExercise: PoseExercise) {

        val poseExerciseList = getMyPoseExerciseList()

        for (i in 0 until poseExerciseList.size) {

            when (poseExerciseList[i].exerciseName) {

                // 이름이 같은 운동이 있으면 갱신해 준다
                poseExercise.exerciseName -> poseExerciseList[i] = poseExercise

            }

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
    fun getAllExerciseList(): ArrayList<PoseExercise> {

        return allExerciseList

    } // getAllExerciseList()


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

        Log.d(TAG, "setUserCheckListHashMap: $userCheckListHashMap")

    } // setUserCheckListHashMap()

}