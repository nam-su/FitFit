package com.example.fitfit.viewModel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.model.SignUpModel

class SignUpViewModel : ViewModel() {

    private val model = SignUpModel()
    val pageCount: MutableLiveData<Int> = MutableLiveData(1)
    val isEmailFocus = MutableLiveData<Boolean>()
    val isCodeFocus = MutableLiveData<Boolean>()
    val isEmailValid = MutableLiveData<Boolean>()




    //다음 버튼 클릭
    fun setOnButtonNextClick(){
        pageCount.value = pageCount.value!! + 1
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
    fun setOnButtonSendClick(){

    }
}