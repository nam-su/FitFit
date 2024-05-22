package com.example.fitfit.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.model.LoginModel
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    val TAG = "로그인 뷰모델"

    private val model = LoginModel()

    val isSuccessLogin = MutableLiveData<Boolean>()

    // 로그인 메서드
    fun login(id: String,password: String){

        viewModelScope.launch {
            when(model.login(id,password,"login")!!.result){

                "failure" -> isSuccessLogin.value = false
                else -> isSuccessLogin.value = true

            }

        }

        Log.d(TAG, "login: " + isSuccessLogin.value)

    } // login()

}