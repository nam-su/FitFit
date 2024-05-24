package com.example.fitfit.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.model.SplashModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(): ViewModel() {

    private val splashModel = SplashModel()

    private val _isCheckLogin = MutableLiveData<Boolean>()
    val isCheckLogin: LiveData<Boolean>
        get() = _isCheckLogin



    // 로그인 정보 확인하는 메서드
    fun checkLogin() {

        when(splashModel.checkLogin()) {

            // 쉐어드에 정보가 있는 경우
            true -> CoroutineScope(Dispatchers.Main).launch { delay(3000)
            _isCheckLogin.value = true
            }
            // 쉐어드에 정보가 없는 경우
            false -> CoroutineScope(Dispatchers.Main).launch { delay(3000)
            _isCheckLogin.value = false
            }

        }

    } // checkLogin()

}