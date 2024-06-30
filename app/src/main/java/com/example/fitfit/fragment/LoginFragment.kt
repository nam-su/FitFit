package com.example.fitfit.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentLoginBinding
import com.example.fitfit.viewModel.LoginViewModel
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginFragment: Fragment() {

    val TAG = "로그인 프래그먼트"
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        KakaoSdk.init(requireContext(),"d997bc71e6bb7cad42042752aa3d4f9f")
        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setListener()
        setObserve()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        loginViewModel = LoginViewModel()
        binding.loginViewModel = loginViewModel

    } // setVariable



    //리스너 설정
    private fun setListener(){

        // 회원가입 버튼 클릭 리스너
        binding.textViewSignUp.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        //비밀번호 찾기 클릭 리스너
        binding.textViewFindPassword.setOnClickListener {
            val bundle = bundleOf("startingPoint" to "loginFragment")
            this.findNavController().navigate(R.id.action_loginFragment_to_findPasswordFragment, bundle)
        }

        binding.ImageButtonKakaoLogin.setOnClickListener {

            setKaKaoLogin()

        }

    }


    // Observe 관련 메서드
    private fun setObserve() {

        // 로그인 버튼 클릭 했을때
        loginViewModel.isSuccessLogin.observe(viewLifecycleOwner) {

            when (it) {

                "success" -> {
                    this.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    (activity as MainActivity).visibleBottomNavi()

                }

                "failure" -> Toast.makeText(activity, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT)
                    .show()

                "disconnect" -> Toast.makeText(activity, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT)
                    .show()

            }

        }
    }

    private fun setKaKaoLogin() {

        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {

            // 카카오톡 로그인
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                // 로그인 실패 부분
                if (error != null) {

                    Log.e(TAG, "로그인 실패 $error")

                    // 사용자가 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {

                        return@loginWithKakaoTalk

                    }
                    // 다른 오류

                    else {

                        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = emailLoginCallback) // 카카오 이메일 로그인

                    }
                }

                // 로그인 성공 부분
                else if (token != null) {

                    Log.e(TAG, "로그인 성공 ${token.accessToken}")

                }

            }
        }

        else {

            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = emailLoginCallback) // 카카오 이메일 로그인

        }

    } // setKaKaoLogin()

    // 이메일 로그인 콜백
    private val emailLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->

        Log.d(TAG, "이메일 로그인 콜백: ")
        
        if (error != null) {
            Log.e(TAG, "로그인 실패 $error")
        } else if (token != null) {
            Log.e(TAG, "로그인 성공 ${token.accessToken}")
        }

    }

}