package com.example.fitfit.network

import com.example.fitfit.function.StringResource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {

    private val stringResource = StringResource.RetrofitStringResource

    private var defaultRetrofit: Retrofit? = null
    private var kakaoRetrofit: Retrofit? = null

    var baseUrl: String = stringResource.baseUrl
    val KAKAOPAY_SECRET_KEY = stringResource.KAKAOPAY_SECRET_KEY


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