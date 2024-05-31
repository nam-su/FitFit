package com.example.fitfit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.model.UserModel

class UserViewModel : ViewModel() {

    private val userModel = UserModel()

    private var _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private var _nickname = MutableLiveData<String>()
    val nickname: LiveData<String>
        get() = _nickname

    private var _loginType = MutableLiveData<String>()
    val loginType: LiveData<String>
        get() = _loginType

    private var _subscribtion = MutableLiveData<String>()
    val subscription: LiveData<String>
        get() = _subscribtion


    // 유저 정보 쉐어드에서 호출
    fun setUserInformation() {

        val user = userModel.getUser()

        _email.value = user.id
        _nickname.value = user.nickname
        _loginType.value = user.loginType
        _subscribtion.value = user.subscribtion

    } // setUserInformation()



    //설정 버튼 누르기
    fun setOnImageButtonSettingClick(){
    }

}