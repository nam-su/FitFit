package com.example.fitfit.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {

    var retrofit: Retrofit? = null

    fun getRetrofitObject(): Retrofit? {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .baseUrl("http://15.164.49.94/")
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        }

        return retrofit

    }

}