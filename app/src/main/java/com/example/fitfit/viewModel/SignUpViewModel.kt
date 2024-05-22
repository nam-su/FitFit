package com.example.fitfit.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.model.SignUpModel
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    val TAG = "회원가입 뷰모델"

    private val model = SignUpModel()
    val pageCount: MutableLiveData<Int> = MutableLiveData(1)
    val course: MutableLiveData<String> = MutableLiveData("duplicateCheck")
    val isEmailFocus = MutableLiveData<Boolean>()
    val isCodeFocus = MutableLiveData<Boolean>()
    val isPasswordFocus = MutableLiveData<Boolean>()
    val isReconfirmPasswordFocus = MutableLiveData<Boolean>()
    val isPasswordCorrect = MutableLiveData<Boolean>()
    val isEmailValid = MutableLiveData<Boolean>()
    val isPasswordValid = MutableLiveData<Boolean>()
    val isEmailPossible = MutableLiveData<Boolean>()
    val isEmailSend = MutableLiveData<Boolean>()
    val isCodeValid = MutableLiveData<Boolean>()
    val isNicknameValid = MutableLiveData<Boolean>()
    val isNicknamePossible = MutableLiveData<Boolean>()
    val isSignUpSuccess = MutableLiveData<Boolean>()
    val signUpEmail = MutableLiveData<String>("")
    val signUpPassword = MutableLiveData<String>("")
    val signUpNickname = MutableLiveData<String>("")




    //다음 버튼 클릭
    fun setOnButtonNextClick(email:String, code:String,password: String,nickname: String){

        Log.d(TAG, "setOnButtonNextClick: ${course.value}")
        //현재 진행상황에 따라 버튼 클릭을 다르게 적용
        when(course.value){

            //진행상황이 중복체크일때
            "duplicateCheck" -> {
                    duplicateCheckId(email)
            }

            //진행상황이 이메일 인증일때
            "emailAuthentication" -> {
                Log.d(TAG, "보낸 인증코드: ${model.randomString}")
                Log.d(TAG, "입력한 인증코드: $code")

                if(model.randomString == code){
                    isCodeValid.value = true
                    course.value = "passwordCheck"
                    pageCount.value = pageCount.value!! + 1
                    signUpEmail.value = email
                }else{
                    isCodeValid.value = false
                }
            }

            //진행상황이 비밀번호 입력일때
            "passwordCheck" -> {
                if(isPasswordCorrect.value == true){
                    course.value = "nicknameCheck"
                    pageCount.value = pageCount.value!! + 1
                    signUpPassword.value = password
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
        pageCount.value = pageCount.value!! - 1
    }



    //이메일 포커스
    fun onEmailFocusChanged(hasFocus: Boolean) {
        isEmailFocus.value = hasFocus
    }



    //코드요청 포커스
    fun onCodeFocusChanged(hasFocus: Boolean) {
        isCodeFocus.value = hasFocus
    }



    //비밀번호 포커스
    fun onPasswordFocusChanged(hasFocus: Boolean) {
        isPasswordFocus.value = hasFocus
    }



    //비밀번호 재확인 포커스
    fun onReconfirmPasswordFocusChanged(hasFocus: Boolean) {
        isReconfirmPasswordFocus.value = hasFocus
    }


    //이메일 유효성 결정
    fun validationEmail(email:String){
        isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }



    //패스워드 유효성 여부
    fun validationPassword(password: String){
        val pattern = Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,15}$")
        isPasswordValid.value = pattern.matches(password)
    }



    //닉네임 유효성 여부
    fun validationNickname(nickname: String){
        val pattern = Regex("^[a-zA-Z가-힣]{3,8}$")
        isNicknameValid.value = pattern.matches(nickname)
    }


    //패스워드 일치 여부
    fun correctPassword(password: String, reconfirmPassword:String){
        isPasswordCorrect.value = password == reconfirmPassword
    }



    //전송 버튼 클릭
    fun setOnButtonSendClick(email:String){

           model.sendMail(email)
            isEmailSend.value = true

    }



    // 아이디 중복검사 메서드
    fun duplicateCheckId(id: String){

        viewModelScope.launch {

            when(model.signUpProcess(id,"","","duplicateCheckId")!!.result){

                "possible" -> {
                    isEmailPossible.value = true
                    course.value = "emailAuthentication"
                }
                "impossible" -> {
                    isEmailPossible.value = false
                    course.value = "duplicateCheck"
                }

            }

        }
    } // duplicateCheckId()



    // 닉네임 중복검사 메서드
    fun duplicateCheckNickname(nickname: String){

        viewModelScope.launch {

            when(model.signUpProcess("","",nickname,"duplicateCheckNickname")!!.result){

                "possible" -> {
                    isNicknamePossible.value = true
                    pageCount.value = pageCount.value!! + 1
                    course.value = "lastCheck"
                    signUpNickname.value = nickname
                }
                "impossible" -> isNicknamePossible.value = false
            }

        }
    } // duplicateCheckId()



    // 완료버튼 ( 서버 저장 + 로그인 프래그먼트로 이동)
    fun setOnButtonCompleteClick(email: String,password: String,nickname: String){
        Log.d(TAG, "setOnButtonCompleteClick: ${email.toString()}")
        Log.d(TAG, "setOnButtonCompleteClick: ${password.toString()}")
        Log.d(TAG, "setOnButtonCompleteClick: ${nickname.toString()}")

        viewModelScope.launch {
            when(model.signUpProcess("email","password","nickname","signUp")!!.result){
                "success" -> isSignUpSuccess.value = true
                else -> isSignUpSuccess.value = false
            }
        }
    }
}