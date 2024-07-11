package com.example.fitfit.fragment

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentPayBinding
import com.example.fitfit.viewModel.PayViewModel

class PayFragment : Fragment() {

    val TAG = "페이 프래그먼트"

    lateinit var binding: FragmentPayBinding
    lateinit var payViewModel: PayViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_pay,container,false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setView()
        setObserve()
        setListener()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        binding.lifecycleOwner = this
        payViewModel = PayViewModel()
        binding.payViewModel = payViewModel

    } // setVariable()


    // 뷰 초기화
    private fun setView() {

        // 취소선 긋기
        binding.textViewMonthBasic.paintFlags = binding.textViewMonthBasic.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.textViewYearBasic.paintFlags = binding.textViewYearBasic.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

    }

    // observe
    private fun setObserve() {

        payViewModel.kakaoPaymentReadyResponse.observe(viewLifecycleOwner) {

            binding.webViewPay.webViewClient = KakaoPayWebViewClient()
            binding.webViewPay.settings.javaScriptEnabled = true
            binding.webViewPay.loadUrl(it.next_redirect_pc_url)

            binding.linearLayoutPayReadyLayout.visibility = View.GONE
            binding.webViewPay.visibility = View.VISIBLE

        }

    } // setObserve()


    private fun setListener() {

        binding.buttonSubscribeDay.setOnClickListener {

            payViewModel.readyKakaoPay("하루 구독권",200)

        }

        binding.buttonSubscribeMonth.setOnClickListener {

            payViewModel.readyKakaoPay("30일 구독권",4200)

        }

        binding.buttonSubscribeYear.setOnClickListener {

            payViewModel.readyKakaoPay("일년 구독권",36500)

        }

    } // setListener()


    inner class KakaoPayWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

            val url = request?.url.toString()
            Log.d(TAG, "shouldOverrideUrlLoading: $url")

            if (url.startsWith("intent://")) {
                Log.d(TAG, "shouldOverrideUrlLoading: intent")
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                startActivity(intent)
                return true

            } else if (url.contains("pg_token=")) {
                Log.d(TAG, "shouldOverrideUrlLoading: pgToken")
                val pgToken = url.substringAfter("pg_token=")

                Log.d(TAG, "shouldOverrideUrlLoading:피지토큰 :  $pgToken")

            } else if (url.contains("cancel")) {

                Log.d(TAG, "shouldOverrideUrlLoading: 캔슬")

            } else if (url.contains("fail")) {

                Log.d(TAG, "shouldOverrideUrlLoading: 실패")

            }

            view!!.loadUrl(url)

            return false

        }

    } // KakaoPayWebViewClient

}