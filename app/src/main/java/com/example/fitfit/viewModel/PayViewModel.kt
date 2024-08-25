package com.example.fitfit.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.PaymentApproveRequest
import com.example.fitfit.data.PaymentReadyRequest
import com.example.fitfit.data.PaymentReadyResponse
import com.example.fitfit.model.PayModel
import kotlinx.coroutines.launch

class PayViewModel: ViewModel() {

    private val payModel = PayModel()

    private val _kakaoPaymentReadyResponse = MutableLiveData<PaymentReadyResponse>()
    val kakaoPaymentReadyResponse: LiveData<PaymentReadyResponse>
    get() = _kakaoPaymentReadyResponse

    private val _payApproveStatus = MutableLiveData<Boolean>()
    val payApproveStatus: LiveData<Boolean>
        get() = _payApproveStatus


    var partner_order : String = ""
    var days = 0

    var userInfo = payModel.getUserInfo()


    // 결제 후 응답 메서드
    fun readyKakaoPay(itemName: String,itemPrice: Int) {

       days = when(itemName){

           payModel.stringResource.oneDaySubscribe -> 1

           payModel.stringResource.monthSubscribe -> 30

           payModel.stringResource.yearSubscribe -> 365

           else -> 0

       }

        viewModelScope.launch {

            partner_order = "${userInfo.nickname}${System.currentTimeMillis()}"

            val paymentReadyRequest = PaymentReadyRequest(

                cid = "TC0ONETIME",
                partner_order_id = partner_order,
                partner_user_id = userInfo.nickname,
                item_name = itemName,
                quantity = 1,
                total_amount = itemPrice,
                vat_amount = 0,
                tax_free_amount = 0,
                approval_url = payModel.stringResource.approval_url,
                fail_url = payModel.stringResource.fail_url,
                cancel_url= payModel.stringResource.cancel_url

            )

            val response = payModel.readyKakaoPay(paymentReadyRequest)

            if(response.isSuccessful && response.body() != null) {

                _kakaoPaymentReadyResponse.value = response.body()

                // 통신 실패의 경우
            } else {


            }

        }

    } // readyKaKaoPay()


    // 카카오페이 결제 요청을 위한 pgToken 업데이트 및 서버에 데이터 수정 요청 및 응답
    suspend fun updatePgToken(pgToken : String) {

            val paymentApproveRequest = PaymentApproveRequest(
                cid = "TC0ONETIME",
                tid = _kakaoPaymentReadyResponse.value!!.tid,
                partner_order_id = partner_order,
                partner_user_id = userInfo.nickname,
                pg_token = pgToken
            )

            val pgTokenResponse = payModel.approveKakaoPay(paymentApproveRequest)

            if(pgTokenResponse.isSuccessful && pgTokenResponse.body() != null){

                _payApproveStatus.value = true
                val response= payModel.updateSubscription(userInfo.id, days)

                if(response.isSuccessful && response.body() != null){

                    payModel.setUser(response.body()!!)

                }

            } else {

                _payApproveStatus.value = false

            }

    } // updatePgToken()

}