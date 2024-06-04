package com.example.fitfit.model

import android.content.Intent
import android.provider.MediaStore
import com.example.fitfit.data.User
import com.example.fitfit.function.MyApplication
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface

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

}