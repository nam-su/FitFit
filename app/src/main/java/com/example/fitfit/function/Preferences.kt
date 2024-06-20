package com.example.fitfit.function

import android.content.Context
import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.User
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Preferences(context: Context) {

    private val TAG = "쉐어드"

    private val preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private val editor = preferences.edit()
    private val allExerciseList = ArrayList<PoseExercise>()
    private val myAllExerciseList = ArrayList<PoseExercise>()
    var name = "하이"

    // 현재 제공하는 모든 운동에 관한 내용 더미 데이터
    init {

        allExerciseList.add(PoseExercise(0,"스쿼트","기본 스쿼트",0,0,0))
        allExerciseList.add(PoseExercise(0,"푸시업","기본 푸시업",0,0,0))
        allExerciseList.add(PoseExercise(0,"런지","기본 런지",0,0,0))

        allExerciseList.add(PoseExercise(0,"스쿼트","와이드 스쿼트",0,0,0))
        allExerciseList.add(PoseExercise(0,"푸시업","와이드 푸시업",0,0,0))
        allExerciseList.add(PoseExercise(0,"런지","왼쪽 런지",0,0,0))

        allExerciseList.add(PoseExercise(0,"스쿼트","내로우 스쿼트",0,0,0))
        allExerciseList.add(PoseExercise(0,"푸시업","내로우 푸시업",0,0,0))
        allExerciseList.add(PoseExercise(0,"런지","오른쪽 런지",0,0,0))

        for (i:Int in 3 until allExerciseList.size) {

            allExerciseList[i].isPrimium = 1

        }

    }


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
            Log.d(TAG, "[set : ${it.category},${it.exerciseName},${it.exerciseCount},${it.goalExerciseCount},${it.date},${it.checkList}]: ")
            }

    }



    // 서버에서 유저 운동 정보 불러온 후 리스트에 저장하는 메서드
    fun setUserExerciseInfoList(exerciseList: ArrayList<PoseExercise>) {

        setMyAllExerciseList(exerciseList)

        CoroutineScope(Dispatchers.Main).launch{

        // 시간 복잡도를 O(n * m) 에서 O(n + m) 으로 줄이기 위한 맵 사용
        // 각 운동의 이름을 key 값으로 해서 맵을 만들고 for문 사용
        val exerciseMap = allExerciseList.associateBy { it.exerciseName }.toMutableMap()

        for (exercise in exerciseList) {

            if (exercise.checkList == 1) {

                exerciseMap[exercise.exerciseName] = exercise

            }

            val myExerciseList = exerciseMap.values.filter { it.checkList == 1 }

//            setMyPoseExerciseList(compareExerciseDate(ArrayList(myExerciseList)))

        }

       }



    } // setUserExerciseInfoList()


    // 최근 운동한 날짜가 오늘과 다르다면 운동 카운트 초기화 하는 메서드
    private fun compareExerciseDate(myExerciseList: ArrayList<PoseExercise>): ArrayList<PoseExercise> {
        // 오늘 날짜
        val todayDate = LocalDate.now()

        // 내 운동리스트 순회
        for (exercise in myExerciseList) {

            // 최근 운동의 운동한 날짜.
            val exerciseDate = LocalDate.ofEpochDay(exercise.date / (24 * 60 * 60 * 1000))

            // 오늘날짜와 마지막 운동 기록의 날짜가 일치하지 않을 때
            if(todayDate != exerciseDate) {

                exercise.date = System.currentTimeMillis()
                exercise.exerciseCount = 0

            }

        }

        return myExerciseList

    } // compareExerciseDate()


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

        val sharedPoseExerciseList = preferences.getString("poseExerciseList","")

        val poseExerciseList = ArrayList<PoseExercise>()

        if(!sharedPoseExerciseList.equals("")) {

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

        editor.putString("poseExerciseList",jsonArray)
        editor.apply()

    } // setExerciseSchedule()


    // 운동 후 리스트 갱신하는 메서드
    fun updateMyPoseExerciseList(poseExercise: PoseExercise) {

        val poseExerciseList = getMyPoseExerciseList()

        for (i in 0 until poseExerciseList.size) {

            when(poseExerciseList[i].exerciseName){

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

        val jsonObject= JSONObject(sharedExerciseName!!)

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