package com.example.fitfit.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.CustomDialogNetworkDisconnectBinding
import com.example.fitfit.databinding.FragmentLoginBinding
import com.example.fitfit.function.MyApplication
import com.example.fitfit.viewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback

class LoginFragment: Fragment() {

    val TAG = "로그인 프래그먼트"
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient

    lateinit var customNetworkDialogBinding: CustomDialogNetworkDisconnectBinding

    private lateinit var callback: OnBackPressedCallback


    // 구글 로그인 런처
    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        loginViewModel.handleGoogleLoginResult(task)

    } // googleAuthLauncher


    // onAttach
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnBackPressed()

    } // onAttach


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        customNetworkDialogBinding = DataBindingUtil.inflate(inflater,R.layout.custom_dialog_network_disconnect,null,false)

        setVariable()

        // 카카오 로그인 SDK 초기화
        KakaoSdk.init(requireContext(),"d997bc71e6bb7cad42042752aa3d4f9f")

        // 네이버 로그인 SDK 초기화
        NaverIdLoginSDK.initialize(activity as MainActivity, "7M1HmHGA6kKvKrXgOScl", "3so4YyCSuU","네이버아이디 로그인")

        // 기존 네이버 토큰 삭제
//        startNaverDeleteToken()

        /**카카오 회원탈퇴 나중에 지우자**/
//        UserApiClient.instance.unlink { error: Throwable? ->
//            Log.d(TAG, "requestKaKaoLogin: $error")
//        }

        // 구글 로그인 관련 초기화
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireContext(),gso)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        // 로그인 버튼 클릭 리스너
        binding.buttonLogin.setOnClickListener {

            // 인터넷 연결 x
            if (!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            // 인터넷 연결 o
            } else {

                loginViewModel.login(
                    binding.editTextLoginInputEmail.text.toString(),
                    binding.editTextLoginInputPassword.text.toString())

            }

        }

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

            // 인터넷 연결 안되어 있을 때
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            } else {

                requestKaKaoLogin()

            }

        }

        // 네이버 로그인 버튼 클릭 리스너
        binding.imageButtonNaverLogin.setOnClickListener {

            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            } else {

                requestNaverLogin()

            }

        }

        // 구글 로그인 버튼 클릭 리스너
        binding.imageButtonGoogleLogin.setOnClickListener {

            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            } else {

                loginViewModel.requestGoogleLogin(gsc,googleAuthLauncher)

            }

        }
        
    } // setListener()


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

                "duplicatedId" -> Toast.makeText(activity, "이미 존재하는 이메일 입니다.", Toast.LENGTH_SHORT).show()

            }

        }

        // 구글 로그인 감지
        loginViewModel.googleLoginResult.observe(viewLifecycleOwner) {

            when(it) {

                "success" -> {}
                "failure" -> {}

            }

        }

    } // setObserve()


    // 카카오 로그인
    private fun requestKaKaoLogin() {

        //카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {

            // 앱으로 로그인
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { oAuthToken, throwable ->

                Log.d(TAG, "requestKaKaoLogin: ${oAuthToken?.idToken}")
                Log.d(TAG, "requestKaKaoLogin: ${oAuthToken?.refreshToken}")
                Log.d(TAG, "requestKaKaoLogin: ${oAuthToken?.accessToken}")
                Log.d(TAG, "requestKaKaoLogin: ${throwable?.message}")

                UserApiClient.instance.me { user, error ->

                    /**서버에 로그인 또는 회원가입 하는 요청 필요**/
                    Log.d(TAG, "requestKaKaoLogin: ${user?.kakaoAccount?.email}")

                    loginViewModel.socialLogin(user?.kakaoAccount?.email.toString(),"kakao")
                }
            }
        } else {
            // 카카오톡이 설치되어 있지 않다면 계정 로그인
            UserApiClient.instance.loginWithKakaoAccount(requireContext()) { oAuthToken, throwable ->
                UserApiClient.instance.me { user, error ->

                    Log.d(TAG, "requestKaKaoLogin: ${user?.kakaoAccount?.email}")
                    Log.d(TAG, "requestKaKaoLogin: $user")

                    loginViewModel.socialLogin(user?.kakaoAccount?.email.toString(),"kakao")


                }
            }
        }


        // 카카오톡 설치 확인
//        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
//
//            // 카카오톡 로그인
//            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
//
//                Log.d(TAG, "requestKaKaoLogin: $token")
//                Log.d(TAG, "requestKaKaoLogin: ${error?.message}")
//
//                when(loginViewModel.requestKakaoApplicationLogin(token, error!!)) {
//
//                    "errorUserCancel" -> return@loginWithKakaoTalk
//
//                    // 다른 에러가 있는 경우 카카오 계정 로그인으로 콜백
//                "elseError" -> UserApiClient.instance.loginWithKakaoAccount(requireContext(),
//                callback = loginViewModel.emailLoginCallback)
//
//                "successLogin" ->  Log.e(TAG, "로그인 성공 ${token?.accessToken}")
//
//                else -> {
//                Log.d(TAG, "requestKaKaoLogin: 하이")}
//
//            }
//
//            }
//
//        }
//
//        // 카카오톡 설치 안된 경우 이메일 콜백 실행
//        else {
//
//            UserApiClient.instance.loginWithKakaoAccount(requireContext(),
//                callback = loginViewModel.emailLoginCallback) // 카카오 이메일 로그인
//
//        }

    } // setKaKaoLogin()


    // 네이버 로그인
    private fun requestNaverLogin() {

        NaverIdLoginSDK.logout()

        NaverIdLoginSDK.authenticate(activity as MainActivity,loginViewModel.requestNaverLogin())

    } // startNaverLogin()


    /** 네이버 아이디 토큰 삭제 정식 구현시 없어져야 함 **/
    private fun startNaverDeleteToken(){

        NidOAuthLogin().callDeleteTokenApi(requireContext(), object : OAuthLoginCallback {

            override fun onSuccess() {

                //서버에서 토큰 삭제에 성공한 상태입니다.
                Toast.makeText(requireContext(), "네이버 아이디 토큰삭제 성공!", Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(httpStatus: Int, message: String) {

                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")

            }

            override fun onError(errorCode: Int, message: String) {

                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)

            }

        })

    } // deleteToken()


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


    // 뒤로가기 눌렀을때
    private fun setOnBackPressed() {

        callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                (activity as MainActivity).finish()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)

    } // onBackPressed()

}