package com.example.fitfit.viewModel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.fitfit.model.UserEditModel
import com.example.fitfit.model.UserModel


class UserEditViewModel {

    private val TAG = "유저 에딧 뷰모델"

    private val userEditModel = UserEditModel()

    private var _nickname = MutableLiveData<String>()
    val nickname: LiveData<String>
        get() = _nickname

    private var _profileImagePath = MutableLiveData<String>()
    val profileImagePath: LiveData<String>
        get() = _profileImagePath

    private val _selectedImageUri = MutableLiveData<Uri>()
    val selectedImageUri: LiveData<Uri>
        get() = _selectedImageUri

    private val _isNicknameValid = MutableLiveData<Boolean>()
    val isNicknameValid: LiveData<Boolean>
        get() = _isNicknameValid

    private val _isCompleteValid = MutableLiveData<Boolean>()
    val isCompleteValid: LiveData<Boolean>
        get() = _isCompleteValid

    // 유저 정보 쉐어드에서 호출
    fun setUserInformation() {

        val user = userEditModel.getUser()

        _nickname.value = user.nickname
        _profileImagePath.value = user.profileImagePath

    } // setUserInformation()



    // 갤러리 인텐트 모델에서 받아오기
    fun getGalleryIntent(): Intent{
       return userEditModel.getGalleryIntent()
    }


    // 갤러리에서 선택한 이미지 uri로 받아오기
    fun setImageUri(uri: Uri, activity:Activity) {
        _selectedImageUri.value = uri
        val contentResolver = activity.application.contentResolver
        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }



    //닉네임 유효성 여부
    fun validationNickname(nickname: String){

        val pattern = Regex("^[가-힣]{3,8}$")

        // (닉네임 3~8자 이내이면서 쉐어드의 닉네임과 다른경우) true
        if(userEditModel.getUser().nickname == nickname){
            _isNicknameValid.value = true
        }else{
            _isNicknameValid.value = pattern.matches(nickname)
        }
    }


    //완료가능한지 여부 체크
    fun validationComplete(value: Boolean){
        _isCompleteValid.value = value
    }
}