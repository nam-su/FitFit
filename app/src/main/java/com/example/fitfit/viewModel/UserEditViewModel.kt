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




    fun setImageUri(uri: Uri, activity:Activity) {
        _selectedImageUri.value = uri
        val contentResolver = activity.application.contentResolver
        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

}