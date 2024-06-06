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

    fun setOnButtonBack(){

    }

    fun isEmailCorrect( inputText : String) : Boolean{
        return model.getUserEmail() == inputText
    }
}