package com.example.fitfit.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.User
import com.example.fitfit.model.SignUpModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    val TAG = "회원가입 뷰모델"

    private val model = SignUpModel()
    val pageCount: MutableLiveData<Int> = MutableLiveData(1)
    val course: MutableLiveData<String> = MutableLiveData("duplicateCheck")
    val isEmailFocus = MutableLiveData<Boolean>()
    val isCodeFocus = MutableLiveData<Boolean>()
    val isEmailValid = MutableLiveData<Boolean>()
    val isEmailPossible = MutableLiveData<Boolean>()
    val isEmailSend = MutableLiveData<Boolean>()
    val isCodeValid = MutableLiveData<Boolean>()


    //다음 버튼 클릭
    fun setOnButtonNextClick(email:String, code:String){

        //현재 진행상황에 따라 버튼 클릭을 다르게 적용
        when(course.value){

            //진행상황이 중복체크일때
            "duplicateCheck" -> {
                    duplicateCheckId(email)
                    if(isEmailPossible.value == false){
                        course.value = "duplicateCheck"
                    }else{
                        course.value = "emailAuthentication"
                    }
            }

            //진행상황이 이메일 인증일때
            "emailAuthentication" -> {
                Log.d(TAG, "보낸 인증코드: ${model.randomString}")
                Log.d(TAG, "입력한 인증코드: ${code.toString()}")

                if(model.randomString == code){
                    isCodeValid.value = true
                    pageCount.value = pageCount.value!! + 1
                }else{
                    isCodeValid.value = false
                }
            }

            //진행상황이 비밀번호 입력일때
            "3" -> {}

            //진행상황이 닉네임 입력일때
            "4" -> {}
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


    //이메일 유효성 결정
    fun validateEmail(str:String){
        isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }



    //전송 버튼 클릭
    fun setOnButtonSendClick(email:String){

           model.sendMail(email)
            isEmailSend.value = true

    }



    // 중복검사 메서드
    fun duplicateCheckId(id: String){

        viewModelScope.launch {
            when(model.duplicateCheckId(id,"","duplicateCheckId")!!.result){

                "possible" -> isEmailPossible.value = true

                "impossible" -> isEmailPossible.value = false

            }

        }


    } // login()
}