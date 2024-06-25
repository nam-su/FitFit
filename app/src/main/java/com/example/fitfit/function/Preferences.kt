package com.example.fitfit.function

import android.content.Context
import android.util.Log
import com.example.fitfit.R
import com.example.fitfit.data.ExerciseInfo
import com.example.fitfit.data.ExerciseItemInfo
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.User
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
    private val allExerciseList = ArrayList<PoseExercise>()
    private val myAllExerciseList = ArrayList<PoseExercise>()

    private val allExerciseItemInfoList = ArrayList<ExerciseItemInfo>()

    // 현재 제공하는 모든 운동에 관한 내용 더미 데이터
    init {

        setAllExerciseList()
        setAllExerciseItemInfoList()

    }

    // 전체 운동 리스트 초기화 하는 메서드
    private fun setAllExerciseList() {

        allExerciseList.add(PoseExercise(0, "스쿼트", "기본 스쿼트", 0, 0, 0))
        allExerciseList.add(PoseExercise(0, "푸시업", "기본 푸시업", 0, 0, 0))
        allExerciseList.add(PoseExercise(0, "런지", "기본 런지", 0, 0, 0))

        allExerciseList.add(PoseExercise(0, "스쿼트", "와이드 스쿼트", 0, 0, 0))
        allExerciseList.add(PoseExercise(0, "레그레이즈", "기본 레그레이즈", 0, 0, 0))
        allExerciseList.add(PoseExercise(0, "런지", "왼쪽 런지", 0, 0, 0))

        allExerciseList.add(PoseExercise(0, "레그레이즈", "왼쪽 레그레이즈", 0, 0, 0))
        allExerciseList.add(PoseExercise(0, "런지", "오른쪽 런지", 0, 0, 0))
        allExerciseList.add(PoseExercise(0, "레그레이즈", "오른쪽 레그레이즈", 0, 0, 0))


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


    // myExerciseList 불러오기
    fun getMyAllExerciseList(): ArrayList<PoseExercise> {

        myAllExerciseList.forEach {
            Log.d(TAG, "get : [${it.category},${it.exerciseName},${it.exerciseCount},${it.goalExerciseCount},${it.date},${it.checkList}]: ")
        }

        return myAllExerciseList

    }


    // myExerciseList 저장
    private fun setMyAllExerciseList(exerciseList: ArrayList<PoseExercise>) {
        Log.d(TAG, "setMyExerciseList 호출")

        myAllExerciseList.clear()
        myAllExerciseList.addAll(exerciseList)

        myAllExerciseList.forEach {
            Log.d(
                TAG,
                "[set : ${it.category},${it.exerciseName},${it.exerciseCount},${it.goalExerciseCount},${it.date},${it.checkList}]: "
            )
        }

    }


    // 서버에서 유저 운동 정보 불러온 후 리스트에 저장하는 메서드
    fun setUserExerciseInfoList(exerciseList: ArrayList<PoseExercise>) {

        setMyAllExerciseList(exerciseList)

        CoroutineScope(Dispatchers.Main).launch {

            // 시간 복잡도를 O(n * m) 에서 O(n + m) 으로 줄이기 위한 맵 사용
            // 각 운동의 이름을 key 값으로 해서 맵을 만들고 for문 사용
            val exerciseMap = allExerciseList.associateBy { it.exerciseName }.toMutableMap()

            for (exercise in exerciseList) {

                if (exercise.checkList == 1) {

                    exerciseMap[exercise.exerciseName] = exercise

                }

                val myExerciseList = exerciseMap.values.filter { it.checkList == 1 }

                setMyPoseExerciseList(ArrayList(myExerciseList))

            }

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

    } // setUser()


    // 스케쥴링한 운동 리스트 불러오는 메서드
    fun getMyPoseExerciseList(): ArrayList<PoseExercise> {

        val sharedPoseExerciseList = preferences.getString("poseExerciseList", "")

        val poseExerciseList = ArrayList<PoseExercise>()

        if (!sharedPoseExerciseList.equals("")) {

            val jsonArray = JSONArray(sharedPoseExerciseList)

            for (i in 0 until jsonArray.length()) {

                val jsonObject: JSONObject = jsonArray.get(i) as JSONObject
                poseExerciseList.add(

                    PoseExercise(

                        jsonObject.get("date").toString().toLong(),
                        jsonObject.get("category").toString(),
                        jsonObject.get("exerciseName").toString(),
                        jsonObject.get("exerciseCount").toString().toInt(),
                        jsonObject.get("goalExerciseCount").toString().toInt(),
                        jsonObject.get("checkList").toString().toInt()

                    )

                )

            }

        }

        return poseExerciseList

    } // getExerciseSchedule()


    // 스케쥴링한 운동 리스트 저장하는 메서드
    fun setMyPoseExerciseList(poseExerciseList: ArrayList<PoseExercise>) {

        val jsonArray = Gson().toJson(poseExerciseList)

        editor.putString("poseExerciseList", jsonArray)
        editor.apply()

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


    // 기존 쉐어드에 운동 정보가 없는 경우 운동 정보 저장.
    fun setPoseExercise(poseExercise: PoseExercise) {

        val jsonObject = Gson().toJson(poseExercise)

        editor.putString(poseExercise.exerciseName, jsonObject)
        editor.apply()

    } // savePoseExercise()


    // 운동 정보 불러오는 메서드
    fun getPoseExercise(exerciseName: String?): PoseExercise {

        val sharedExerciseName = preferences.getString(exerciseName, "")

        val jsonObject = JSONObject(sharedExerciseName!!)

        return PoseExercise(

            jsonObject.get("date").toString().toLong(),
            jsonObject.get("category").toString(),
            jsonObject.get("exerciseName").toString(),
            jsonObject.get("exerciseCount").toString().toInt(),
            jsonObject.get("goalExerciseCount").toString().toInt(),
            jsonObject.get("checkList").toString().toInt()

        )

    } // loadPoseExercise


    //로그아웃 또는 회원탈퇴 시 쉐어드 모든 데이터 삭제
    fun removeAll() {
        editor.clear()
        editor.apply()
    }


    // 제공되는 운동 리스트 리턴
    fun getAllExerciseList(): ArrayList<PoseExercise> {

        return allExerciseList

    }

}