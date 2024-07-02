package com.example.fitfit.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentLoginBinding
import com.example.fitfit.viewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginFragment: Fragment() {

    val TAG = "로그인 프래그먼트"
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)

        KakaoSdk.init(requireContext(),"d997bc71e6bb7cad42042752aa3d4f9f")

        NaverIdLoginSDK.initialize(activity as MainActivity, "7M1HmHGA6kKvKrXgOScl", "3so4YyCSuU","네이버아이디 로그인")

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireContext(),gso)

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

        // 카카오 로그인 버튼 클릭 리스너
        binding.imageButtonKakaoLogin.setOnClickListener {

            requestKaKaoLogin()

        }

        // 네이버 로그인 버튼 클릭 리스너
        binding.imageButtonNaverLogin.setOnClickListener {

            requestNaverLogin()

        }

        // 구글 로그인 버튼 클릭 리스너
        binding.imageButtonGoogleLogin.setOnClickListener {

            requestGoogleLogin()

        }
        
    } // setListener()


    // 구글 로그인 런처
    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {

            val account = task.getResult(ApiException::class.java)
            Log.d(TAG, "로그인 성공: ${account.email}")

        } catch (e: ApiException) {

            Log.e(TAG, "로그인 실패: ${e.statusCode}", e)

        }

    } // googleAuthLauncher


    // 구글 로그인 요청 메서드
    private fun requestGoogleLogin() {

        gsc.signOut()
        val signInIntent = gsc.signInIntent
        googleAuthLauncher.launch(signInIntent)

    } // requestGoogleLogin()


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


    // 카카오 로그인
    private fun requestKaKaoLogin() {

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


    // 카카오 이메일 로그인 콜백
    private val emailLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->

        Log.d(TAG, "이메일 로그인 콜백: ")
        
        if (error != null) {
            Log.e(TAG, "로그인 실패 $error")
        } else if (token != null) {
            Log.e(TAG, "로그인 성공 ${token.accessToken}")
        }

    } // emailLoginCallback


    // 네이버 로그인
    private fun requestNaverLogin() {

        var naverToken :String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {

            override fun onSuccess(response: NidProfileResponse) {

                val userId = response.profile?.id

                Log.d(TAG, "onSuccess:id: ${userId} \\ntoken: ${naverToken}")

                Toast.makeText(requireContext(), "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(httpStatus: Int, message: String) {

                val errorCode = NaverIdLoginSDK.getLastErrorCode().code

                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

                Toast.makeText(requireContext(), "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()

            }

            override fun onError(errorCode: Int, message: String) {

                onFailure(errorCode, message)

            }

        }

        /** OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. */
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()
//                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
//                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
//                var naverTokenType = NaverIdLoginSDK.getTokenType()
//                var naverState = NaverIdLoginSDK.getState().toString()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)

            }

            override fun onFailure(httpStatus: Int, message: String) {

                val errorCode = NaverIdLoginSDK.getLastErrorCode().code

                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

                Toast.makeText(requireContext(), "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()

            }

            override fun onError(errorCode: Int, message: String) {

                onFailure(errorCode, message)

            }

        }

        NaverIdLoginSDK.authenticate(activity as MainActivity, oauthLoginCallback)

    } // startNaverLogin()

}