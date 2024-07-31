package com.example.fitfit.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.function.MyApplication
import com.example.fitfit.model.ExerciseEditModel
import kotlinx.coroutines.launch

class ExerciseEditViewModel: ViewModel() {

    private val TAG = "운동 편집 뷰모델"
    private val exerciseEditModel = ExerciseEditModel()

    private val _checkMyExerciseListSizeMax = MutableLiveData<Boolean>()
    val checkMyExerciseListSizeMax: LiveData<Boolean>
        get() = _checkMyExerciseListSizeMax

    private val _myExerciseListSize = MutableLiveData<Int>()
    val myExerciseListSize: LiveData<Int>
        get() = _myExerciseListSize

    private val _isSuccessfulEdit = MutableLiveData<Boolean>()
    val isSuccessfulEdit: LiveData<Boolean>
        get() = _isSuccessfulEdit


    // 내 운동 리스트
    fun getSharedMyExerciseList(): ArrayList<PoseExercise> {

        _myExerciseListSize.value = exerciseEditModel.myExerciseList.size

        return exerciseEditModel.getSharedMyExerciseList()

    } // setMyExerciseList()


    // 모든 스쿼트 리스트
    fun setAllSquatList(): ArrayList<PoseExercise> = exerciseEditModel.getAllSquatList()
    // setAllSquatList()


    // 모든 푸시업 리스트
    fun setAllPushUpList(): ArrayList<PoseExercise> = exerciseEditModel.getAllPushUpList()
    // setAllPushUpList()


    // 모든 런지 리스트
    fun setAllLungeList(): ArrayList<PoseExercise> = exerciseEditModel.getAllLungeList()
    // setAllLungeList()


    // 모든 레그레이즈 리스트
    fun setAllLegRaisesList(): ArrayList<PoseExercise> = exerciseEditModel.getAllLegRaisesList()
    // setAllLegRaisesList()


    // 리스트에서 아이템 삭제
    fun deleteExerciseItem(myExerciseList: ArrayList<PoseExercise>,position: Int) {

        exerciseEditModel.deleteExerciseItem(myExerciseList,position)

        _myExerciseListSize.value = exerciseEditModel.myExerciseList.size

    } // deleteExerciseItem()


    // 리스트에 아이템 추가
    fun addExerciseItem(poseExerciseList: ArrayList<PoseExercise>,position: Int):Boolean {

        // 여기서 리스트에 아이템이 프리미엄인지 판독 우선
        if(poseExerciseList[position].isPrimium == 1) {

            // 구독이 되어 있는 경우
            return if(MyApplication.sharedPreferences.getUser().subscription != "") {

                _checkMyExerciseListSizeMax.value = exerciseEditModel.addExerciseItem(poseExerciseList[position])

                _myExerciseListSize.value = exerciseEditModel.myExerciseList.size

                true

            // 구독이 안되어 있는 경우
            } else {

                Log.d(TAG, "addExerciseItem: 여기로 와야됨")
                false

            }

        } else {

            _checkMyExerciseListSizeMax.value = exerciseEditModel.addExerciseItem(poseExerciseList[position])

            _myExerciseListSize.value = exerciseEditModel.myExerciseList.size

            return true

        }

    } // addExerciseItem()


    // 내 운동 리스트가 최소 3개인지 판별
    fun checkMyExerciseListSizeMin(): Boolean = exerciseEditModel.myExerciseList.size > 2
    // checkMyExerciseListSizeMin()


    //서버에 체크리스트를 담은 객체를 보내고 응답을 받는 메서드
    fun setMyPoseExercise(){

        viewModelScope.launch {

            val response = exerciseEditModel.setMyPoseExerciseList()

            if (response.isSuccessful && response.body() != null) {

                when(response.body()!!.result){

                    "success" -> _isSuccessfulEdit.value = true
                    else -> _isSuccessfulEdit.value = false

                }
                
            } else {

                println("Failed to send data. Error code: ${response.code()}")

            }

        }

    } //setMyPoseExercise()


    // model에 해시맵 변경 요청
    fun setUserCheckList(){

        exerciseEditModel.setUserCheckList()

    } // setUserCheckList()


    // 갱신한 리스트를 싱글톤에 저장하는 메서드를 모델에 요청
    fun setMyPoseExerciseList(){

        exerciseEditModel.setMyExerciseList()

    } // setMyPoseExerciseList()


}