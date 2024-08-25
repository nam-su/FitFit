package com.example.fitfit.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.model.FindPasswordModel
import com.example.fitfit.model.SignUpModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.math.log
import kotlin.time.ExperimentalTime

class FindPasswordViewModel : ViewModel() {

    private val model = FindPasswordModel()

    private val _isEmailValid = MutableLiveData<Boolean>()
    val isEmailValid: LiveData<Boolean>
        get() = _isEmailValid

    private val _isCodeValid = MutableLiveData<String>()
    val isCodeValid: LiveData<String>
        get() = _isCodeValid

    private val _timerCount = MutableLiveData<Int>()
    val timerCount: LiveData<Int>
        get() = _timerCount

    private val _isEmailSend = MutableLiveData<Boolean>()
    val isEmailSend: LiveData<Boolean>
        get() = _isEmailSend

    private val _mode = MutableLiveData<String>("emailCheck")
    val mode: LiveData<String>
        get() = _mode

    private val _isPasswordValid = MutableLiveData<Boolean>()
    val isPasswordValid: LiveData<Boolean>
        get() = _isPasswordValid

    private val _isEmailPossible = MutableLiveData<Boolean>()
    val isEmailPossible: LiveData<Boolean>
        get() = _isEmailPossible

    private val _isPasswordCorrect = MutableLiveData<Boolean>()
    val isPasswordCorrect: LiveData<Boolean>
        get() = _isPasswordCorrect

    private val _startingPoint = MutableLiveData<String>()
    val startingPoint: LiveData<String>
        get() = _startingPoint

    private val _isPasswordDuplicated = MutableLiveData<Boolean>()
    val isPasswordDuplicated: LiveData<Boolean>
        get() = _isPasswordDuplicated

    private lateinit var job: Job


    // 뒤로가기 버튼 클릭했을때 메서드
    fun setOnBackButtonClick(){

        when(mode.value){

            "complete" -> setMode("passwordChange")

            "passwordChange" -> setMode("emailCheck")

            "codeCheck" -> setMode("emailCheck")

            "emailCheck" -> setMode("close")

            else -> {}

        }

    } // setOnBackButtonClick()


    //이메일 유효성 결정
    fun validationEmail(email:String){

        _isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    } // validationEmail()


    /**다음버튼 클릭 메서드
     * 클릭할때마다 모드 변경**/
    fun setOnNextButtonClick(id: String,code:String ,password: String) {

        viewModelScope.launch {

            when (mode.value) {

                "emailCheck" -> {

                    if (isEmailExist(id)) {

                        setMode("codeCheck")

                        model.sendMail(id)

                        _isEmailPossible.value = true

                        //타이머 시작
                        timerStart()

                        _isEmailSend.value = true

                    } else {

                        _isEmailPossible.value = false

                    }

                }

                "codeCheck" -> {

                    when(isCodeValid(code)){

                        true -> {

                            setMode("passwordChange")

                            timerStop()

                        }

                        false -> {}

                    }

                }

                "passwordChange" -> {

                    if(passwordReset(id,password)){

                        setMode("complete")

                        model.setSharedPreferencesRemoveUserInfo()

                    }

                }

                else -> {}

            }

        }

    } // setOnNextButtonClick()


    // 아이디 중복 검사 메서드
    private suspend fun isEmailExist(id: String): Boolean {

        return withContext(Dispatchers.IO) {

            val result = async {

                when(model.passwordResetProcess(id, "", "isMyEmailCheck")!!.result){

                    "success" -> true

                    else -> false

                }

            }

            result.await()

        }

    } // duplicateCheckId()


    // 비밀번호 변경
    private suspend fun passwordReset(id: String, password: String): Boolean {

        return withContext(Dispatchers.IO) {

            val result = async {

                when(model.passwordResetProcess(id, password, "passwordUpdate")!!.result){

                    "success" -> true
                    "duplicated" -> {
                        _isPasswordDuplicated.postValue(true)
                        false
                    }
                    else -> false

                }

            }

            result.await()

        }

    } // duplicateCheckId()


    // 모드 변경
    private fun setMode(value: String){ _mode.value = value }
    //setMode()


    // 생성된 코드가 유효한지 확인하는 메소드
    private fun isCodeValid(code:String): Boolean {

        // 현재 시간과 코드 생성 시간의 차이를 계산하여 1분(60초) 이내인지 확인
        if(model.randomString == code && code != "") {

            val currentTime = System.currentTimeMillis()

            if((currentTime - model.codeGeneratedTime) <= model.timeLimit * 1000){

                _isCodeValid.value = "true"

                return true

            }else{

                _isCodeValid.value = "expired"

            }

        } else {

            _isCodeValid.value = "wrong"

        }

        return false // 1분(60초) 이내인지 확인

    } // isCodeValid()


    // 타이머 시작
    private fun timerStart(){

        timerStop()

        //타이머 초기값
        _timerCount.value = model.timeLimit

        job = viewModelScope.launch {

            while(_timerCount.value!! > 0) {

                _timerCount.value = _timerCount.value!!.minus(1)

                delay(1000L)

            }

        }

    } // timerStart()


    // 타이머 중지
    private fun timerStop(){

        if(::job.isInitialized) job.cancel()

    } // timerStop()


    // 전송 버튼 클릭
    fun setOnButtonSendClick(email:String){

        model.sendMail(email)
        _isEmailSend.value = true

        timerStart()

    } // setOnButtonSendClick()


    // 타이머 포맷으로 바꾸기
    fun setTimerFormat(time:Int):String{

        val minutes = time.div(60)
        val seconds = time.rem(60)

        return "%02d:%02d".format(minutes, seconds)

    } // setTimerFormat


    // 패스워드 일치 여부
    fun correctPassword(password: String, reconfirmPassword:String){

        _isPasswordCorrect.value = password == reconfirmPassword

    } // correctPassword()


    // 패스워드 유효성 여부
    fun validationPassword(password: String){

        val pattern = Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,15}$")
        _isPasswordValid.value = pattern.matches(password)

    } // validationPassword()


    // 스타팅 포인트 저장
    fun setStartingPoint(startingPoint: String){

        _startingPoint.value = startingPoint

    } // setStartingPoint()

}