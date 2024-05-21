package com.example.fitfit.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.fitfit.model.SplashModel

class SplashViewModel {

    private val model = SplashModel()

    private val isCheckLogin = MutableLiveData<Boolean>()

    fun checkLogin() {

        when(model.loginInfo()) {

            // 쉐어드에 정보가 있는 경우
            true -> isCheckLogin.value = true
            // 쉐어드에 정보가 없는 경우
            false -> isCheckLogin.value = false

        }

    }

}