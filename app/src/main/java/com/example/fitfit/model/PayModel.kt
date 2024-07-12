package com.example.fitfit.model

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
    suspend fun readyKakaoPay(itemName: String,itemPrice: Int): Response<PaymentReadyResponse> {

        val paymentReadyRequest = PaymentReadyRequest(

            cid = "TC0ONETIME",
            partner_order_id = "partner_order_id",
            partner_user_id = "partner_user_id",
            item_name = itemName,
            quantity = 1,
            total_amount = itemPrice,
            vat_amount = 0,
            tax_free_amount = 0,
            approval_url = "http://15.164.103.25/pay_test.php",
            fail_url = "http://15.164.103.25/pay_fail.php",
            cancel_url="http://15.164.103.25/pay_cancel.php"

        )

        return retrofitKakaoInterface.readyKakaoPay(paymentReadyRequest)

    }


    // 유저 정보 불러오기
    fun getUserInfo(): User = MyApplication.sharedPreferences.getUser()
    // getUserInfo()

}