package com.example.fitfit.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {

    var retrofit: Retrofit? = null
    var baseUrl: String? = "http://15.164.49.94/"

    // 레트로핏 객체 초기화 하는 메서드
    fun getRetrofitObject(): Retrofit? {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        }

        return retrofit

    } // getRetrofitObject()


    // 카카오 페이를 위한 레트로핏 객체 초기화
    fun getKakaoRetrofitObject(): Retrofit? {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .baseUrl("https://open-api.kakaopay.com/online/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        }

        return retrofit

    }

}