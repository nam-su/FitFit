package com.example.fitfit.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.model.FindPasswordModel
import com.example.fitfit.model.SignUpModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log
import kotlin.time.ExperimentalTime

class FindPasswordViewModel : ViewModel() {

    val TAG = "회원가입 뷰모델"

    private val model = FindPasswordModel()

    private val _isEmailCorrect = MutableLiveData<Boolean>()
    val isEmailCorrect: LiveData<Boolean>
        get() = _isEmailCorrect

    private val _mode = MutableLiveData<String>()
    val mode: LiveData<String>
        get() = _mode

    fun setOnButtonBack(){

    }

    fun isEmailCorrect( inputText : String) : Boolean{
        return model.getUserEmail() == inputText
    }



    /**다음버튼 클릭 메서드
     * 클릭할때마다 모드 변경**/
    fun setOnNextButtonClick(){
        when(mode.value){
            "passwordChange" -> setMode("emailVerification")
            "passwordReset" -> setMode("emailVerification")
            "emailVerification" -> setMode("password")
            "password" -> setMode("complete")
            else -> {}

        }
    } //setOnNextButtonClick()



    //모드 변경
    fun setMode(value: String){ _mode.value = value }
    //setMode()
}