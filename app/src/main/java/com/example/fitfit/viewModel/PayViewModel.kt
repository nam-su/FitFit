package com.example.fitfit.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.PaymentReadyResponse
import com.example.fitfit.model.PayModel
import kotlinx.coroutines.launch

class PayViewModel: ViewModel() {

    private val TAG = "페이 뷰모델"

    private val payModel = PayModel()

    private val _kakaoPaymentReadyResponse = MutableLiveData<PaymentReadyResponse>()
    val kakaoPaymentReadyResponse: LiveData<PaymentReadyResponse>
    get() = _kakaoPaymentReadyResponse

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


}