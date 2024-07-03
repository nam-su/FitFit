package com.example.fitfit.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.function.MyApplication
import com.example.fitfit.model.SplashModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(): ViewModel() {

    val TAG = "스플래시 뷰모델"

    private val splashModel = SplashModel()

    private val _isCheckLogin = MutableLiveData<Boolean>()
    val isCheckLogin: LiveData<Boolean>
        get() = _isCheckLogin



    // 로그인 정보 확인하는 메서드
    fun checkLogin() {

        when(splashModel.checkLogin()) {

            // 쉐어드에 정보가 있는 경우
            true -> CoroutineScope(Dispatchers.Main).launch {

                selectUserExercise()
                delay(3000)
                _isCheckLogin.value = true

            }

            // 쉐어드에 정보가 없는 경우
            false -> CoroutineScope(Dispatchers.Main).launch { delay(3000)
            _isCheckLogin.value = false

            }

        }

    } // checkLogin()


    // 서버에 요청한 유저 운동정보 응답 받는 메서드
    fun selectUserExercise() {

        viewModelScope.launch {

            val response = splashModel.selectUserExercise()

            Log.d(TAG, "selectUserExercise: ${response.isSuccessful}")
            Log.d(TAG, "selectUserExercise: ${response.body()}")
            Log.d(TAG, "selectUserExercise: ${response.message()}")

            if(response.isSuccessful && response.body() != null) {

                //스플래시 성공으로인해 데이터 받으면
                //1. 데이터를 일단 싱글 톤에 저장 해야함(1. 운동 전체 리스트 저장, 2. 체크리스트 저장)
                //유저운동정보를 저장하는 메서드 호출
                splashModel.saveUserExerciseInfo(response.body()!!.userAllExerciseList!!)
                splashModel.saveUserCheckList(response.body()!!.checkList!!)

                 /** 스플래시 모델에서 싱글톤에 저장하는 메서드 호출**/
                splashModel.saveChallengeList(response.body()!!.challengeList!!)

            } else {

                Log.d(TAG, "selectUserExercise: 통신 실패")

            }

        }

    } // selectUserExercise()

}