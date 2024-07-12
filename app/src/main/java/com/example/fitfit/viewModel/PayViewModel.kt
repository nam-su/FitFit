package com.example.fitfit.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.PaymentReadyRequest
import com.example.fitfit.data.PaymentReadyResponse
import com.example.fitfit.model.PayModel
import kotlinx.coroutines.launch

class PayViewModel: ViewModel() {

    private val TAG = "페이 뷰모델"

    private val payModel = PayModel()

    private val _kakaoPaymentReadyResponse = MutableLiveData<PaymentReadyResponse>()
    val kakaoPaymentReadyResponse: LiveData<PaymentReadyResponse>
    get() = _kakaoPaymentReadyResponse

    var partner_order : String = ""

    var userInfo = payModel.getUserInfo()


    // 결제 후 응답 메서드
    fun readyKakaoPay(itemName: String,itemPrice: Int) {

        viewModelScope.launch {

            val response = payModel.readyKakaoPay(itemName,itemPrice)

            Log.d(TAG, "readyKakaoPay: ${response.code()}")
            Log.d(TAG, "readyKakaoPay: ${response.errorBody()}")

            Log.d(TAG, "readyKaKaoPay: ${response.message()}")
            Log.d(TAG, "readyKaKaoPay: ${response.isSuccessful}")
            Log.d(TAG, "readyKaKaoPay: ${response.body()}")

            if(response.isSuccessful && response.body() != null) {

                Log.d(TAG, "readyKakaoPay: 통신성공")
                _kakaoPaymentReadyResponse.value = response.body()

                // 통신 실패의 경우
            } else {



            }

        }

    } // readyKaKaoPay()


    // 카카오페이 결제 요청을 위한 pgToken 업데이트 및 서버에 데이터 수정 요청 및 응답
    fun updatePgToken(pgToken : String) {

//        val map = HashMap<String, String>()
//        map["cid"] = "TC0ONETIME"
//        map["tid"] = _kakaoPaymentReadyResponse.value!!.tid
//        map["partner_order_id"] = partner_order
//        map["partner_user_id"] = userInfo.nickname
//        map["pg_token"] = pgToken
//
//        payModel.approveKakaoPay(map).enqueue(object : Callback<KakaoPayLoad> {
//            override fun onResponse(call: Call<KakaoPayLoad>, response: Response<KakaoPayLoad>) {
//                if (response.isSuccessful) {
//
//                    Log.d("PayViewModel", "item_name : ${response.body()!!.item_name}")
//                    val product = response.body()!!.item_name
//
//                    model.updateTicket(userInfo.nickname, product).enqueue(object : Callback<UserInfo> {
//                        override fun onResponse(call: Call<UserInfo>, response2: Response<UserInfo>) {
//                            if (response.isSuccessful) {
//                                Log.d("PayViewModel", "onResponse: ${response2.body()}")
//                                liveDataPayApproveStatus.value = true
//                                liveDataUpdateUserInfo.value = response2.body()
//
//                            } else {
//                                liveDataPayApproveStatus.value = false
//                                Log.d("PayViewModel", "updateTicket - onResponse isFailure : ${response.errorBody()?.string()}")
//                            }
//                        }
//
//                        override fun onFailure(call: Call<UserInfo>, t: Throwable) {
//                            liveDataPayApproveStatus.value = false
//                            Log.d("PayViewModel", "updateTicket - onFailure : $t")
//                        }
//
//                    })
//
//                } else {
//                    liveDataPayApproveStatus.value = false
//                    Log.d("PayViewModel", "updatePgToken - onResponse isFailure: ${response.errorBody()?.string()}")
//                }
//            }
//            override fun onFailure(call: Call<KakaoPayLoad>, t: Throwable) {
//                liveDataPayApproveStatus.value = false
//                Log.d("PayViewModel", "updatePgToken - onFailure: $t")
//            }
//
//        })

    } // updatePgToken()

}