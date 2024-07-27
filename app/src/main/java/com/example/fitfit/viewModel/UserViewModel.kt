package com.example.fitfit.viewModel


import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.function.MyApplication
import com.example.fitfit.model.UserModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    private var _profileImagePath = MutableLiveData<String>()
    val profileImagePath: LiveData<String>
        get() = _profileImagePath

    private var _subscription = MutableLiveData<String>()
    val subscription: LiveData<String>
        get() = _subscription

    private var _selectedMenuItem = MutableLiveData<MenuItem>()
    val selectedMenuItem: LiveData<MenuItem>
        get() = _selectedMenuItem

    private var _isLogoutButtonClick = MutableLiveData<Boolean>()
    val isLogoutButtonClick: LiveData<Boolean>
        get() = _isLogoutButtonClick

    private var _isProgressButtonClick = MutableLiveData<Boolean>()
    val isProgressButtonClick: LiveData<Boolean>
        get() = _isProgressButtonClick

    private var _isWithdrawalButtonClick = MutableLiveData<Boolean>()
    val isWithdrawalButtonClick: LiveData<Boolean>
        get() = _isWithdrawalButtonClick

    private var _isWithdrawalSuccess = MutableLiveData<String>()
    val isWithdrawalSuccess: LiveData<String>
        get() = _isWithdrawalSuccess


    // 유저 정보 쉐어드에서 호출
    fun setUserInformation(baseUrl: String) {

        val user = userModel.getUser()

        _email.value = user.id
        _nickname.value = user.nickname
        _loginType.value = user.loginType
        _subscription.value = user.subscription
        _profileImagePath.value = baseUrl+user.profileImagePath

    } // setUserInformation()


    //체크된 아이템 저장
    fun selectItem(item: MenuItem) {

        _selectedMenuItem.value = item

    } // selectItem()


    // 로그인 성공했을때 Shared에 데이터 추가해준다.
    fun setOnDialogOkButtonClick(buttonOkText: String) {

        when(buttonOkText){

            "로그아웃" -> {

                userModel.setSharedPreferencesRemoveUserInfo()
                setIsLogoutButtonClick(true)
                MyApplication.sharedPreferences.getAllExerciseList()

            }

            "회원탈퇴" -> {

                setIsWithdrawalButtonClick(true)

            }

            "진행" -> {

                setIsProgressButtonClick(true)

                Log.d(TAG, "setOnDialogOkButtonClick: 진행")

            }

        }

    } // setSharedPreferencesUserInfo()


    //_isLogoutButtonClick 값 변경
    fun setIsLogoutButtonClick(value:Boolean){ _isLogoutButtonClick.value = value }
    // setIsLogoutButtonClick()


    //_isLogoutButtonClick 값 변경
    fun setIsProgressButtonClick(value:Boolean){ _isProgressButtonClick.value = value }
    // setIsProgressButtonClick()


    //_isWithdrawalButtonClick 값 변경
    fun setIsWithdrawalButtonClick(value:Boolean){ _isWithdrawalButtonClick.value = value }
    // setIsWithdrawalButtonClick()


    //회원탈퇴 통신 메서드
    fun withdrawal(){

        /**서버 데이터 삭제 메서드**/
        viewModelScope.launch {

            Log.d(TAG, "코루틴 시작")
            val response =  userModel.withdrawalProcess(userModel.getUser().id,"withdrawal")

            if(response.isSuccessful && response.body() != null) {

                val user = response.body()!!

                when(user.result){

                    "failure" -> _isWithdrawalSuccess.value = "failure"
                    else -> {

                        _isWithdrawalSuccess.value = "success"
                        userModel.setSharedPreferencesRemoveUserInfo()
                    }

                }

                // 통신 실패의 경우
            } else {

                Log.d(TAG, "withdrawal: ${response.message()}")
                Log.d(TAG, "withdrawal: ${response.isSuccessful}")
                Log.d(TAG, "withdrawal: ${response.body()}")
                _isWithdrawalSuccess.value = "disconnect"

            }

        }

        Log.d(TAG, "setOnDialogOkButtonClick: 회원탈퇴")

    } // withdrawal()


    //구독권 만료 삭제 메서드
    fun deleteSubscription(){

        /**서버 데이터 삭제 메서드**/
        viewModelScope.launch {

            val response =  userModel.deleteSubscription()

            if(response.isSuccessful && response.body() != null) {

                    userModel.setUser(response.body()!!)

                // 통신 실패의 경우
            } else {

                Log.d(TAG, "withdrawal: ${response.message()}")
                Log.d(TAG, "withdrawal: ${response.isSuccessful}")
                Log.d(TAG, "withdrawal: ${response.body()}")

            }

        }

        Log.d(TAG, "setOnDialogOkButtonClick: 회원탈퇴")

    } // withdrawal()


    // 구독권 기간이 만료 여부 판단 메서드
    fun isExpiredSubscription(): Boolean {

        if(subscription.value != "") {
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

            // 문자열을 LocalDate로 변환
            val date = LocalDate.parse(subscription.value, formatter)

            // 오늘 날짜 가져오기
            val today = LocalDate.now()

            return date.isBefore(today)
        }

        return false

    } //isExpiredSubscription()
}