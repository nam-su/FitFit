package com.example.fitfit.model

import com.example.fitfit.data.KakaoPayLoad
import com.example.fitfit.data.PaymentApproveRequest
import com.example.fitfit.data.PaymentReadyRequest
import com.example.fitfit.data.PaymentReadyResponse
import com.example.fitfit.data.User
import com.example.fitfit.function.MyApplication
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Response

class PayModel {

    val TAG = "페이 모델"

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitInterface: RetrofitInterface = retrofitBuilder.getRetrofitObject()!!.create(RetrofitInterface::class.java)
    private val retrofitKakaoInterface: RetrofitInterface = retrofitBuilder.getKakaoRetrofitObject()!!.create(RetrofitInterface::class.java)


    // 카카오페이 준비 단계
    suspend fun readyKakaoPay(paymentReadyRequest: PaymentReadyRequest): Response<PaymentReadyResponse> {

        return retrofitKakaoInterface.readyKakaoPay(paymentReadyRequest)

    } // readyKakaoPay()


    // 카카오페이 준비 단계
    suspend fun approveKakaoPay(paymentApproveRequest: PaymentApproveRequest): Response<KakaoPayLoad> {

        return retrofitKakaoInterface.approveKakaoPay(paymentApproveRequest)

    } //approveKakaoPay()


    // 결제 완료 후 웹 서버에 이용권 업데이트 요청
    suspend fun updateSubscription(id : String, days : Int) = retrofitInterface.updateSubscription(id, days)

    // 유저 정보 불러오기
    fun getUserInfo(): User = MyApplication.sharedPreferences.getUser()
    // getUserInfo()


    //유저 정보 저장하기
    fun setUser(user: User) = MyApplication.sharedPreferences.setUser(user)
}