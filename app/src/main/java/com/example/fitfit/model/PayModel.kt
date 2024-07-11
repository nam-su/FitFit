package com.example.fitfit.model

import android.util.Log
import com.example.fitfit.data.PaymentReadyRequest
import com.example.fitfit.data.PaymentReadyResponse
import com.example.fitfit.network.RetrofitBuilder
import com.example.fitfit.network.RetrofitInterface
import retrofit2.Call
import retrofit2.Response
import retrofit2.create

class PayModel {

    val TAG = "페이 모델"

    private val retrofitBuilder = RetrofitBuilder()
    private val retrofitKakaoInterface: RetrofitInterface = retrofitBuilder.getKakaoRetrofitObject()!!.create(RetrofitInterface::class.java)


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

}