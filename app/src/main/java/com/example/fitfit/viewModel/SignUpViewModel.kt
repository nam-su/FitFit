package com.example.fitfit.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.User
import com.example.fitfit.model.SignUpModel
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    val TAG = "회원가입 뷰모델"

    private val model = SignUpModel()
    val pageCount: MutableLiveData<Int> = MutableLiveData(1)
    val isEmailFocus = MutableLiveData<Boolean>()
    val isCodeFocus = MutableLiveData<Boolean>()
    val isEmailValid = MutableLiveData<Boolean>()
    val isEmailPossible = MutableLiveData<Boolean>()


    //다음 버튼 클릭
    fun setOnButtonNextClick(email:String){
        when(pageCount.value){
            1 -> {
                if(isEmailPossible.value == true){
                    pageCount.value = pageCount.value!! + 1
                }else{
                    duplicateCheckId(email)
                }

            }
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