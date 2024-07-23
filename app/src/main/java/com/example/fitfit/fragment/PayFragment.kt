package com.example.fitfit.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.CustomDialogNetworkDisconnectBinding
import com.example.fitfit.databinding.FragmentPayBinding
import com.example.fitfit.function.MyApplication
import com.example.fitfit.viewModel.PayViewModel
import kotlinx.coroutines.launch

class PayFragment : Fragment() {

    val TAG = "페이 프래그먼트"

    lateinit var binding: FragmentPayBinding
    lateinit var payViewModel: PayViewModel

    lateinit var customNetworkDialogBinding: CustomDialogNetworkDisconnectBinding

    private lateinit var callback: OnBackPressedCallback


    // onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_pay,container,false)
        customNetworkDialogBinding = DataBindingUtil.inflate(inflater,R.layout.custom_dialog_network_disconnect,null,false)

        return binding.root

    } // onCreateView()


    // onViewCreated
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

    } // setView()


    // observe
    @SuppressLint("SetJavaScriptEnabled")
    private fun setObserve() {

        payViewModel.kakaoPaymentReadyResponse.observe(viewLifecycleOwner) {

            binding.webViewPay.webViewClient = KakaoPayWebViewClient()
            binding.webViewPay.settings.javaScriptEnabled = true
            binding.webViewPay.loadUrl(it.next_redirect_mobile_url)

            binding.linearLayoutPayReadyLayout.visibility = View.GONE
            binding.webViewPay.visibility = View.VISIBLE

        }

        // 결제 승인 확인
        payViewModel.payApproveStatus.observe(viewLifecycleOwner) {

            binding.webViewPay.visibility = View.GONE
            binding.linearLayoutPayReadyLayout.visibility = View.VISIBLE

            when(it) {

                true -> Toast.makeText(requireActivity(), "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show()

                false ->  Toast.makeText(requireActivity(), "결제에 실패하였습니다.", Toast.LENGTH_SHORT).show()

            }

        }

    } // setObserve()


    // 리스너 초기화
    private fun setListener() {

        binding.buttonSubscribeDay.setOnClickListener {

            // 인터넷 연결 x
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            // 인터넷 연결 o
            } else {

                payViewModel.readyKakaoPay("하루 구독권",200)

            }

        }

        binding.buttonSubscribeMonth.setOnClickListener {

            // 인터넷 연결 x
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            // 인터넷 연결 o
            } else {

                payViewModel.readyKakaoPay("30일 구독권",4200)

            }

        }

        binding.buttonSubscribeYear.setOnClickListener {
            // 인터넷 연결 x
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            // 인터넷 연결 o
            } else {

                payViewModel.readyKakaoPay("일년 구독권",36500)

            }

        }

    } // setListener()


    // shouldOverrideUrlLoading 메서드 사용하기 위한 이너 클래스
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

                lifecycleScope.launch {
                    payViewModel.updatePgToken(pgToken)
                }

            } else if (url.contains("cancel")) {

                Log.d(TAG, "shouldOverrideUrlLoading: 캔슬")

            } else if (url.contains("fail")) {

                Log.d(TAG, "shouldOverrideUrlLoading: 실패")

            }

            view!!.loadUrl(url)

            return false

        }

    } // KakaoPayWebViewClient


    //커스텀 다이얼로그 띄우기
    private fun setNetworkCustomDialog(){

        // 부모가 있는지 확인하고, 있다면 부모에서 제거
        customNetworkDialogBinding.root.parent?.let {
            (it as ViewGroup).removeView(customNetworkDialogBinding.root)
        }

        //다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(customNetworkDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customNetworkDialogBinding.textViewButtonOk.setOnClickListener {

            dialog.dismiss()

        }

        dialog.setOnCancelListener {

            dialog.dismiss()

        }

        dialog.show()

    } // setNetworkCustomDialog()


    // 뒤로가기 버튼 눌렀을 때
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                findNavController().popBackStack()
                (activity as MainActivity).visibleBottomNavi()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    } // onBackPressed()

}