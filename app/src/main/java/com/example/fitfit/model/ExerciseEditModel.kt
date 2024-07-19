package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.SplashResponse
import com.example.fitfit.function.MyApplication
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import com.google.gson.Gson
import retrofit2.Response

class ExerciseEditModel {

    val TAG = "유저 운동 편집 모델"

    val myExerciseList: ArrayList<PoseExercise> = MyApplication.sharedPreferences.getMyPoseExerciseList() ?: ArrayList()
    private val allExerciseList = MyApplication.sharedPreferences.getAllExerciseList()
    private val allSquatList = ArrayList<PoseExercise>()
    private val allPushUpList = ArrayList<PoseExercise>()
    private val allLungeList = ArrayList<PoseExercise>()
    private val allLegRaisesList = ArrayList<PoseExercise>()

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface =
        retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)

    init {

        setAllCategoryList()

    }

    // 내 운동리스트 리턴하는 메서드
    fun getSharedMyExerciseList(): ArrayList<PoseExercise> = myExerciseList
    // setMyExerciseList()


    // 전체 운동 리스트에서 카테고리별 리스트에 객체 추가
    private fun setAllCategoryList() {

        for (exercise in allExerciseList) {

            when(exercise.category){

                "스쿼트" -> allSquatList.add(exercise)

                "푸시업" -> allPushUpList.add(exercise)

                "런지" ->  allLungeList.add(exercise)

                "레그레이즈" -> allLegRaisesList.add(exercise)

            }

        }

        allSquatList.removeAll{ poseExercise -> myExerciseList.any {it.exerciseName == poseExercise.exerciseName} }
        allPushUpList.removeAll{ poseExercise -> myExerciseList.any {it.exerciseName == poseExercise.exerciseName} }
        allLungeList.removeAll{ poseExercise -> myExerciseList.any {it.exerciseName == poseExercise.exerciseName} }
        allLegRaisesList.removeAll{ poseExercise -> myExerciseList.any {it.exerciseName == poseExercise.exerciseName} }

    } // setAllCategoryList()


    // 모든 스쿼트 리스트 리턴
    fun getAllSquatList(): ArrayList<PoseExercise> = allSquatList
     // getAllSquatList()


    // 모든 푸시업 리스트 리턴하는 메서드
    fun getAllPushUpList(): ArrayList<PoseExercise> = allPushUpList
     // getAllPushUpList()


    // 모든 런지 리스트 리턴하는 메서드
    fun getAllLungeList(): ArrayList<PoseExercise> = allLungeList
    // getAllLungeList()


    // 모든 레그레이즈 리스트 리턴하는 메서드
    fun getAllLegRaisesList(): ArrayList<PoseExercise> =allLegRaisesList
    // getAllLegRaisesList()


    // 내 운동 리스트에서 아이템 삭제하는 메서드
    fun deleteExerciseItem(myExerciseList: ArrayList<PoseExercise>,position: Int){

        when(myExerciseList[position].category) {

            "스쿼트" -> allSquatList.add(myExerciseList[position])
            "푸시업" -> allPushUpList.add(myExerciseList[position])
            "런지" -> allLungeList.add(myExerciseList[position])
            "레그레이즈" -> allLegRaisesList.add(myExerciseList[position])

        }

        myExerciseList.removeAt(position)

    } // deleteExerciseItem()


    // 내 운동 리스트에 아이템 추가하는 메서드
    fun addExerciseItem(poseExercise: PoseExercise): Boolean {

        return if(myExerciseList.size < 20) {

            myExerciseList.add(poseExercise)
            removeExerciseListItem(poseExercise)

            true

        } else {

            false

        }

    } // addExerciseItem()


    // 운동별 리스트에서 아이템 삭제되는 메서드
    private fun removeExerciseListItem(exercise: PoseExercise) {

        when(exercise.category) {

            "스쿼트" -> allSquatList.removeAll{ poseExercise ->
                myExerciseList.any {it.exerciseName == poseExercise.exerciseName} }

            "푸시업" -> allPushUpList.removeAll{ poseExercise ->
                myExerciseList.any {it.exerciseName == poseExercise.exerciseName} }

            "런지" ->  allLungeList.removeAll{ poseExercise ->
                myExerciseList.any {it.exerciseName == poseExercise.exerciseName} }

            "레그레이즈" -> allLegRaisesList.removeAll{ poseExercise ->
                myExerciseList.any{it.exerciseName == poseExercise.exerciseName} }

        }

    } // removeExerciseListItem


    //서버에 arrayList 전송
    suspend fun setMyPoseExerciseList(): Response<SplashResponse> {

        val id = MyApplication.sharedPreferences.getUserId()
        val userCheckListHashMap = LinkedHashMap<String,Int>()

        // 해시맵에 전체 리스트 담기
        allExerciseList.forEach {

            userCheckListHashMap[it.exerciseName] = 0

        }

        //내 리스트는 1로 변경
        myExerciseList.forEach {

            //해시맵에서 제거 후 맨뒤로 저장
            userCheckListHashMap.remove(it.exerciseName)
            userCheckListHashMap[it.exerciseName] = 1

        }

        //0값들 맨뒤로 보내기
        moveZeroValuesToEnd(userCheckListHashMap)

        //제이슨문자열 형태로 바꾸기
        val hashMapToJsonString = hashMapToJsonString(userCheckListHashMap)

        // 쉐어드의 리스트 바꾸기
        MyApplication.sharedPreferences.setAllExerciseList(hashMapToJsonString)

        return retrofitInterface.setMyCheckList(id, hashMapToJsonString,"updateList")

    } //setMyPoseExerciseList()


    //해시맵 제이슨문자열로 바꾸기
    private fun hashMapToJsonString(hashMap: LinkedHashMap<String,Int>): String{

        //gson 객체 생성
        val gson = Gson()

        //해시맵을 JSON 문자열로 변환
        return gson.toJson(hashMap)

    } //hashMapToJson()


    //hashMap 변경
    fun setUserCheckList() = MyApplication.sharedPreferences.setUserCheckListHashMap(myExerciseList)
    // setUserCheckList()


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


    // 싱글톤에 내 리스트 저장
    fun setMyExerciseList(){

        MyApplication.sharedPreferences.setMyExerciseList(myExerciseList)

    } // setMyExerciseList()


}