package com.example.fitfit.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.model.SignUpModel
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    val TAG = "회원가입 뷰모델"

    private val model = SignUpModel()
    private val _pageCount: MutableLiveData<Int> = MutableLiveData(1)
    val pageCount: LiveData<Int>
        get() = _pageCount

    private val _course: MutableLiveData<String> = MutableLiveData("duplicateCheck")
    val course: LiveData<String>
        get() = _course

    private val _isEmailFocus = MutableLiveData<Boolean>()
    val isEmailFocus: LiveData<Boolean>
        get() = _isEmailFocus

    private val _isCodeFocus = MutableLiveData<Boolean>()
    val isCodeFocus: LiveData<Boolean>
        get() = _isCodeFocus

    private val _isPasswordFocus = MutableLiveData<Boolean>()
    val isPasswordFocus: LiveData<Boolean>
        get() = _isPasswordFocus

    private val _isReconfirmPasswordFocus = MutableLiveData<Boolean>()
    val isReconfirmPasswordFocus: LiveData<Boolean>
        get() = _isReconfirmPasswordFocus

    private val _isPasswordCorrect = MutableLiveData<Boolean>()
    val isPasswordCorrect: LiveData<Boolean>
        get() = _isPasswordCorrect

    private val _isEmailValid = MutableLiveData<Boolean>()
    val isEmailValid: LiveData<Boolean>
        get() = _isEmailValid

    private val _isPasswordValid = MutableLiveData<Boolean>()
    val isPasswordValid: LiveData<Boolean>
        get() = _isPasswordValid

    private val _isEmailPossible = MutableLiveData<Boolean>()
    val isEmailPossible: LiveData<Boolean>
        get() = _isEmailPossible

    private val _isEmailSend = MutableLiveData<Boolean>()
    val isEmailSend: LiveData<Boolean>
        get() = _isEmailSend

    private val _isCodeValid = MutableLiveData<Boolean>()
    val isCodeValid : LiveData<Boolean>
        get() = _isCodeValid

    private val _isNicknameValid = MutableLiveData<Boolean>()
    val isNicknameValid: LiveData<Boolean>
        get() = _isNicknameValid

    private val _isNicknamePossible = MutableLiveData<Boolean>()
    val isNicknamePossible: LiveData<Boolean>
        get() = _isNicknamePossible

    private val _isSignUpSuccess = MutableLiveData<Boolean>()
    val isSignUpSuccess: LiveData<Boolean>
        get() = _isSignUpSuccess

    private val _signUpEmail = MutableLiveData<String>("")
    val signUpEmail: LiveData<String>
        get() = _signUpEmail

    private val _signUpPassword = MutableLiveData<String>("")
    val signUpPassword: LiveData<String>
        get() = _signUpPassword

    private val _signUpNickname = MutableLiveData<String>("")
    val signUpNickname: LiveData<String>
        get() = _signUpNickname


    //다음 버튼 클릭
    fun setOnButtonNextClick(email:String, code:String,password: String,nickname: String){

        Log.d(TAG, "setOnButtonNextClick: ${_course.value}")
        //현재 진행상황에 따라 버튼 클릭을 다르게 적용
        when(_course.value){

            //진행상황이 중복체크일때
            "duplicateCheck" -> {
                    duplicateCheckId(email)
            }

            //진행상황이 이메일 인증일때
            "emailAuthentication" -> {
                Log.d(TAG, "보낸 인증코드: ${model.randomString}")
                Log.d(TAG, "입력한 인증코드: $code")
                Log.d(TAG, "setOnButtonNextClick: ${isCodeValid()}")
                when(model.randomString == code && code != "" && isCodeValid()){
                    true -> {
                        _pageCount.value = _pageCount.value!! + 1
                        _signUpEmail.value = email
                        Log.d(TAG, "뷰모델: 요기")}
                    false -> {
                        _signUpEmail.value = ""
                        Log.d(TAG, "뷰모델: 유효x")
                    }

                }

            }

            //진행상황이 비밀번호 입력일때
            "passwordCheck" -> {
                if(isPasswordCorrect.value == true){
                    _pageCount.value = _pageCount.value!! + 1
                    _signUpPassword.value = password
                }
            }

            //진행상황이 닉네임 입력일때
            "nicknameCheck" -> {
                duplicateCheckNickname(nickname)
            }
            else -> {}
        }
    }



    //뒤로가기 버튼 클릭
    fun setOnButtonBack(){
        if(pageCount.value!! > 0) {
            _pageCount.value = pageCount.value!! - 1
        }
    }



    //이메일 포커스
    fun onEmailFocusChanged(hasFocus: Boolean) {
        _isEmailFocus.value = hasFocus
    }



    //코드요청 포커스
    fun onCodeFocusChanged(hasFocus: Boolean) {
        _isCodeFocus.value = hasFocus
    }



    //비밀번호 포커스
    fun onPasswordFocusChanged(hasFocus: Boolean) {
        _isPasswordFocus.value = hasFocus
    }



    //비밀번호 재확인 포커스
    fun onReconfirmPasswordFocusChanged(hasFocus: Boolean) {
        _isReconfirmPasswordFocus.value = hasFocus
    }


    //이메일 유효성 결정
    fun validationEmail(email:String){
        _isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }



    //패스워드 유효성 여부
    fun validationPassword(password: String){
        val pattern = Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,15}$")
        _isPasswordValid.value = pattern.matches(password)
    }



    //닉네임 유효성 여부
    fun validationNickname(nickname: String){
        val pattern = Regex("^[가-힣]{3,8}$")
        _isNicknameValid.value = pattern.matches(nickname)
    }


    //패스워드 일치 여부
    fun correctPassword(password: String, reconfirmPassword:String){
        _isPasswordCorrect.value = password == reconfirmPassword
    }



    //전송 버튼 클릭
    fun setOnButtonSendClick(email:String){

           model.sendMail(email)
            _isEmailSend.value = true

    }



    // 아이디 중복 검사 메서드
    private fun duplicateCheckId(id: String){

        viewModelScope.launch {

            when(model.signUpProcess(id,"","","duplicateCheckId")!!.result){

                "possible" -> {
                    _isEmailPossible.value = true
                    _course.value = "emailAuthentication"

                    //메일도 동시에 보내기
                    model.sendMail(id)
                    _isEmailSend.value = true
                }
                "impossible" -> {
                    _isEmailPossible.value = false
                    _course.value = "duplicateCheck"
                }

            }

        }
    } // duplicateCheckId()



    // 닉네임 중복검사 메서드
    private fun duplicateCheckNickname(nickname: String){

        viewModelScope.launch {

            when(model.signUpProcess("","",nickname,"duplicateCheckNickname")!!.result){

                "possible" -> {
                    _isNicknamePossible.value = true
                    _pageCount.value = _pageCount.value!! + 1
                    _signUpNickname.value = nickname
                }
                "impossible" -> _isNicknamePossible.value = false
            }

        }
    } // duplicateCheckId()



    // 완료버튼 ( 서버 저장 + 로그인 프래그먼트로 이동)
    fun setOnButtonCompleteClick(email: String,password: String,nickname: String){
        Log.d(TAG, "setOnButtonCompleteClick: $email")
        Log.d(TAG, "setOnButtonCompleteClick: $password")
        Log.d(TAG, "setOnButtonCompleteClick: $nickname")

        viewModelScope.launch {
            when(model.signUpProcess(email,password,nickname,"signUp")!!.result){
                "success" -> _isSignUpSuccess.value = true
                else -> _isSignUpSuccess.value = false
            }
        }
    }


    //course value 변경
    fun changeCourseValue(value:String){
        _course.value = value
    }



    // 생성된 코드가 유효한지 확인하는 메소드
    private fun isCodeValid(): Boolean {
        // 현재 시간과 코드 생성 시간의 차이를 계산하여 1분(60초) 이내인지 확인
        val currentTime = System.currentTimeMillis()
        _isCodeValid.value = (currentTime - model.codeGeneratedTime) <= 10 * 1000
        return _isCodeValid.value!! // 1분(60초) 이내인지 확인
    }
}