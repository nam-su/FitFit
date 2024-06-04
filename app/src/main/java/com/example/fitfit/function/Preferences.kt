package com.example.fitfit.function

import android.content.Context
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.User
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class Preferences(context: Context) {

    private val preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private val editor = preferences.edit()
    private val allExerciseList = ArrayList<PoseExercise>()

    init {

        allExerciseList.add(PoseExercise(0,"스쿼트","기본 스쿼트",0,0,0))
        allExerciseList.add(PoseExercise(0,"푸시업","기본 푸시업",0,0,0))
        allExerciseList.add(PoseExercise(0,"런지","기본 런지",0,0,0))

        allExerciseList.add(PoseExercise(0,"스쿼트","1번 스쿼트",0,0,0))
        allExerciseList.add(PoseExercise(0,"푸시업","1번 푸시업",0,0,0))
        allExerciseList.add(PoseExercise(0,"런지","1번 런지",0,0,0))

        allExerciseList.add(PoseExercise(0,"스쿼트","2번 스쿼트",0,0,0))
        allExerciseList.add(PoseExercise(0,"푸시업","2번 푸시업",0,0,0))
        allExerciseList.add(PoseExercise(0,"런지","2번 런지",0,0,0))

        allExerciseList.add(PoseExercise(0,"스쿼트","3번 스쿼트",0,0,0))
        allExerciseList.add(PoseExercise(0,"푸시업","3번 푸시업",0,0,0))
        allExerciseList.add(PoseExercise(0,"런지","3번 런지",0,0,0))

        allExerciseList.add(PoseExercise(0,"스쿼트","4번 스쿼트",0,0,0))
        allExerciseList.add(PoseExercise(0,"푸시업","4번 푸시업",0,0,0))
        allExerciseList.add(PoseExercise(0,"런지","4번 런지",0,0,0))

        allExerciseList.add(PoseExercise(0,"스쿼트","5번 스쿼트",0,0,0))

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


    // 유저 정보 쉐어드에 저장 하는 메서드
    fun setUser(user: User) {

        editor.putString("id", user.id)
        editor.putString("nickname", user.nickname)
        editor.putString("loginType", user.loginType)
        editor.putString("profileImagePath", user.profileImagePath)
        editor.putString("subscription", user.subscription)
        editor.apply()

    } // setUser()



    //유저 정보 쉐어드에서 삭제하는 메서드
    fun removeUser(){

        editor.remove("id")
        editor.remove("nickname")
        editor.remove("loginType")
        editor.remove("profileImagePath")
        editor.remove("subscription")
        editor.apply()

    } //removeUser()


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


    // 제공되는 운동 리스트 리턴
    fun getAllExerciseList(): ArrayList<PoseExercise> {

        return allExerciseList

    }

}