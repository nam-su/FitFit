package com.example.fitfit.function

import android.content.Context
import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.User
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class Preferences(context: Context) {

    private val preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private val editor = preferences.edit()


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
    fun getPoseExerciseList(): ArrayList<PoseExercise> {

        val sharedPoseExerciseList = preferences.getString("poseExerciseList","")

        val poseExerciseList = ArrayList<PoseExercise>()

        if(!sharedPoseExerciseList.equals("")) {

            val jsonArray = JSONArray(sharedPoseExerciseList)

            for (i in 0 until jsonArray.length()) {

                val jsonObject: JSONObject = jsonArray.get(i) as JSONObject
                poseExerciseList.add(

                    PoseExercise(

                        jsonObject.get("date").toString(),
                        jsonObject.get("category").toString(),
                        jsonObject.get("exerciseName").toString(),
                        jsonObject.get("exerciseCount").toString().toInt(),
                        jsonObject.get("goalExerciseCount").toString().toInt()

                    )

                )

            }

        }

        return poseExerciseList

    } // getExerciseSchedule()


    // 스케쥴링한 운동 리스트 저장하는 메서드
    fun setPoseExerciseList(poseExerciseList: ArrayList<PoseExercise>) {

        val jsonArray = Gson().toJson(poseExerciseList)

        editor.putString("poseExerciseList",jsonArray)
        editor.apply()

    } // setExerciseSchedule()


    // 운동 후 리스트 갱신하는 메서드
    fun updatePoseExerciseList(poseExercise: PoseExercise) {

        val poseExerciseList = getPoseExerciseList()

        for (i in 0 until poseExerciseList.size) {

            when(poseExerciseList[i].exerciseName){

                poseExercise.exerciseName -> poseExerciseList[i]= poseExercise

            }

        }

        setPoseExerciseList(poseExerciseList)

    } // updatePoseExerciseList()


    // 기존 쉐어드에 운동 정보가 없는 경우 운동 정보 저장.
    fun setPoseExercise(poseExercise: PoseExercise) {

        val jsonObject = Gson().toJson(poseExercise)

        Log.d("쉐어드에 저장", "updatePoseExercise: 날짜 : ${poseExercise.date}")
        Log.d("쉐어드에 저장", "updatePoseExercise: 카운트 : ${poseExercise.exerciseCount}")
        Log.d("쉐어드에 저장", "updatePoseExercise: 목표 : ${poseExercise.goalExerciseCount}")
        Log.d("쉐어드에 저장", "updatePoseExercise: 이름 : ${poseExercise.exerciseName}")
        Log.d("쉐어드에 저장", "updatePoseExercise: 분류 : ${poseExercise.category}")

        editor.putString(poseExercise.exerciseName, jsonObject)
        editor.apply()

    } // savePoseExercise()


    // 운동 정보 불러오는 메서드
    fun getPoseExercise(exerciseName: String?): PoseExercise {

        val sharedExerciseName = preferences.getString(exerciseName, "")

        val jsonObject= JSONObject(sharedExerciseName!!)

        return PoseExercise(

            jsonObject.get("date").toString(),
            jsonObject.get("category").toString(),
            jsonObject.get("exerciseName").toString(),
            jsonObject.get("exerciseCount").toString().toInt(),
            jsonObject.get("goalExerciseCount").toString().toInt()

        )

    } // loadPoseExercise



    //로그아웃 또는 회원탈퇴 시 쉐어드 모든 데이터 삭제
    fun removeAll(){
        editor.clear()
        editor.apply()
    }

}