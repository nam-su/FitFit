package com.example.fitfit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfit.model.SignUpModel

class SignUpViewModel : ViewModel() {

    private val model = SignUpModel()
    val pageCount: MutableLiveData<Int> = MutableLiveData(1)

    private val _emailFocus = MutableLiveData<Boolean>()
    val emailFocus: LiveData<Boolean>
        get() = _emailFocus

    private val _codeFocus = MutableLiveData<Boolean>()
    val codeFocus: LiveData<Boolean>
        get() = _codeFocus



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
        _emailFocus.value = hasFocus
    }



    //코드요청 포커스
    fun onCodeFocusChanged(hasFocus: Boolean) {
        _codeFocus.value = hasFocus
    }
}