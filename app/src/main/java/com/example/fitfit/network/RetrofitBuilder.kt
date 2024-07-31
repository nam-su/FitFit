package com.example.fitfit.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {

    private var defaultRetrofit: Retrofit? = null
    private var kakaoRetrofit: Retrofit? = null

    var baseUrl: String = "http://15.164.49.94/"
    val KAKAOPAY_SECRET_KEY = "DEVF70FE81BA55A6924A2361A24B570781466812"

    // 레트로핏 객체 초기화 하는 메서드
    fun getRetrofitObject(): Retrofit? {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        if (defaultRetrofit == null) {

            defaultRetrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        }

        return defaultRetrofit

    } // getRetrofitObject()


    // 카카오 페이를 위한 레트로핏 객체 초기화
    fun getKakaoRetrofitObject(): Retrofit? {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        if (kakaoRetrofit == null) {

            kakaoRetrofit = Retrofit.Builder()
                .baseUrl("https://open-api.kakaopay.com/online/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        }

        return kakaoRetrofit

    }

}