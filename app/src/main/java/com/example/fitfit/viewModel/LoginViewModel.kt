package com.example.fitfit.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.User
import com.example.fitfit.model.LoginModel
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {

    val TAG = "로그인 뷰모델"

    private val loginModel = LoginModel(application.applicationContext)

    val isSuccessLogin = MutableLiveData<String>()


    // 로그인 메서드
    fun login(id: String,password: String){

        viewModelScope.launch {

            val response = loginModel.login(id,password,"login")

            if(response.isSuccessful && response.body() != null) {

                val user = response.body()!!

                when(user.result){

                        "failure" -> isSuccessLogin.value = "failure"
                        else -> setSharedPreferencesUserinfo(user)

                    }

                // 통신 실패의 경우
            } else {

                Log.d(TAG, "login: ${response.message()}")
                Log.d(TAG, "login: ${response.isSuccessful}")
                Log.d(TAG, "login: ${response.body()}")
                isSuccessLogin.value = "disconnect"

            }

        }

    } // login()


    // 로그인 성공했을때 Shared에 데이터 추가해준다.
    private fun setSharedPreferencesUserinfo(user: User) {

        isSuccessLogin.value = "success"
        loginModel.setSharedPreferencesUserInfo(user)

    } // setSharedPreferencesUserInfo()

}