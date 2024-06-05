package com.example.fitfit.model

import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.User
import com.example.fitfit.function.MyApplication
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class UserEditModel {

    private val TAG = "유저 에딧 모델"

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(
        RetrofitInterface::class.java)


    fun getUser(): User {
        return MyApplication.sharedPreferences.getUser()
    }


    //
    fun getGalleryIntent(): Intent {
        val intent = Intent(Intent.ACTION_PICK).apply {
            action = Intent.ACTION_OPEN_DOCUMENT
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        return intent
    }


    // 프로필 수정한 값 서버에 저장
    suspend fun profileEdit(image: MultipartBody.Part?, id: RequestBody, nickname: RequestBody): Response<User> {

        return retrofitInterface.profileEdit(image,id,nickname)

    } // withdrawalProcess()



    // 쉐어드에 유저정보 저장.
    fun setSharedPreferencesUserInfo(user: User) {

        MyApplication.sharedPreferences.setUser(user)

    } // setSharedPreferencesUserInfo()



    // 레트로핏에서 baseurl 경로 받아오기
    fun getBaseUrl(): String{
        return retrofitBuilder.baseUrl.toString()
    }


}