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

    private var _subscription = MutableLiveData<String>()
    val subscription: LiveData<String>
        get() = _subscription

    private var _selectedMenuItem = MutableLiveData<Int>()
    val selectedMenuItem: LiveData<Int>
        get() = _selectedMenuItem

    // 유저 정보 쉐어드에서 호출
    fun setUserInformation() {

        val user = userModel.getUser()

        _email.value = user.id
        _nickname.value = user.nickname
        _loginType.value = user.loginType
        _subscription.value = user.subscription

    } // setUserInformation()



    //체크된 아이템 저장
    fun selectItem(itemId: Int) {
        _selectedMenuItem.value = itemId
    }



    // 로그인 성공했을때 Shared에 데이터 추가해준다.
    fun setOnLogoutButtonClick() {

        userModel.setSharedPreferencesRemoveUserInfo()

    } // setSharedPreferencesUserInfo()

}