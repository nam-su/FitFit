package com.example.fitfit.viewModel

import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.model.UserModel

class UserViewModel : ViewModel() {

    private val TAG = "유저 뷰모델"

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

    private var _selectedMenuItem = MutableLiveData<MenuItem>()
    val selectedMenuItem: LiveData<MenuItem>
        get() = _selectedMenuItem

    private var _isLogout = MutableLiveData<Boolean>()
    val isLogout: LiveData<Boolean>
        get() = _isLogout


    // 유저 정보 쉐어드에서 호출
    fun setUserInformation() {

        val user = userModel.getUser()

        _email.value = user.id
        _nickname.value = user.nickname
        _loginType.value = user.loginType
        _subscription.value = user.subscription

    } // setUserInformation()



    //체크된 아이템 저장
    fun selectItem(item: MenuItem) {
        _selectedMenuItem.value = item
    }



    // 로그인 성공했을때 Shared에 데이터 추가해준다.
    fun setOnDialogOkButtonClick(buttonOkText: String) {

        when(buttonOkText){
            "로그아웃" -> {
                userModel.setSharedPreferencesRemoveUserInfo()
                setIsLogout(true)
            }
            "회원탈퇴" -> Log.d(TAG, "setOnDialogOkButtonClick: 회원탈퇴")
            "진행" -> Log.d(TAG, "setOnDialogOkButtonClick: 확인")

        }


    } // setSharedPreferencesUserInfo()




    //isLogout 값 변경
    fun setIsLogout(value:Boolean){
        _isLogout.value = value
    }

}