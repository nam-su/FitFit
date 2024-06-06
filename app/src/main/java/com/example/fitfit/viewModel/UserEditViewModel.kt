package com.example.fitfit.viewModel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.User
import com.example.fitfit.model.UserEditModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class UserEditViewModel :ViewModel() {

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


    private val _profileEditResult = MutableLiveData<String>()
    val profileEditResult: LiveData<String>
        get() = _profileEditResult

    var baseUrl: String = userEditModel.getBaseUrl()

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



    // 프로필 수정 메서드
    fun profileEdit(activity: Activity, nickname: String){

        Log.d(TAG, "profileEdit: 1")
        viewModelScope.launch {

            Log.d(TAG, "profileEdit: 2")

            var mode:String = if(selectedImageUri.value != null){ "withImage" }else{ "withoutImage" }

            val image = if(selectedImageUri.value != null){
                val imageFile = File(getRealPathFromUri(activity, selectedImageUri.value))
                val requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)
                MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
            } else{ null }

               Log.d(TAG, "profileEdit: 3")
            // 문자열 값을 RequestBody로 변환합니다.
            Log.d(TAG, "profileEdit: $image")
            Log.d(TAG, "profileEdit: $mode")

            val requestBodyId: RequestBody = RequestBody.create(MediaType.parse("text/plain"), userEditModel.getUser().id)
            val requestBodyNickname: RequestBody = RequestBody.create(MediaType.parse("text/plain"), nickname)
            val requestBodyMode: RequestBody = RequestBody.create(MediaType.parse("text/plain"), mode)

            val response = if(selectedImageUri.value != null){
                userEditModel.profileEdit(image!!,requestBodyId,requestBodyNickname, requestBodyMode)
            }else{
                userEditModel.profileEditWithoutImage(userEditModel.getUser().id,nickname,mode)
            }

            Log.d(TAG, "profileEdit: ${response.isSuccessful}")
            Log.d(TAG, "profileEdit: ${response.body()!!.result}")

            if(response.isSuccessful && response.body() != null){

                    _profileEditResult.value = response.body()!!.result

                // 이미지업로드와 닉네임변경 성공했을 때만 쉐어드 갱신
                if(_profileEditResult.value == "success"){
                    setSharedPreferencesUserinfo(response.body()!!)
                    setUserInformation()
                }
            }else{
                _profileEditResult.value = "failure"
            }

        }
    } // profileEdit()


    //uri에서 realpath 뽑아오기
    private fun getRealPathFromUri(activity: Activity, uri: Uri?): String? {

        var filePath: String? = null
        var parcelFileDescriptor: ParcelFileDescriptor? = null
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null

            try {
                parcelFileDescriptor = activity.contentResolver.openFileDescriptor(uri!!, "r")
                if (parcelFileDescriptor != null) {
                    fileInputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)

                    // 파일을 저장할 임시 디렉토리를 만든다.
                    val tempDir = activity.applicationContext.cacheDir
                    val tempFile =
                        File.createTempFile("profile" + userEditModel.getUser().id, null, tempDir)

                    // 파일을 복사합니다.
                    fileOutputStream = FileOutputStream(tempFile)
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead)
                    }

                    // 파일 경로 설정
                    filePath = tempFile.absolutePath
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                // 리소스 해제
                try {
                    fileInputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    fileOutputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    parcelFileDescriptor?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


        Log.d(TAG, "getRealPathFromUri: $filePath")
        return filePath
    }



    // 프로필 수정 성공했을때 Shared에 데이터 추가해준다.
    private fun setSharedPreferencesUserinfo(user: User) {
        Log.d(TAG, "setSharedPreferencesUserinfo: 여그?")
        userEditModel.setSharedPreferencesUserInfo(user)

    } // setSharedPreferencesUserInfo()

}