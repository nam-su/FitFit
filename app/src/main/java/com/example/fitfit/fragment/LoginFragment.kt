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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback

class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient

    lateinit var customNetworkDialogBinding: CustomDialogNetworkDisconnectBinding

    private lateinit var callback: OnBackPressedCallback

    var loginType:String = ""


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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        customNetworkDialogBinding = DataBindingUtil.inflate(inflater,R.layout.custom_dialog_network_disconnect,null,false)

        setVariable()

        /** 키값 스트링 파일로 **/
        // 네이버 로그인 SDK 초기화
        NaverIdLoginSDK.initialize(activity as MainActivity,
            getString(R.string.naverClientId),
            getString(R.string.naverClientSecret),
            getString(R.string.naverClientName))

        setBundle()

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


    // 프래그먼트 이동시 번들에 데이터 받아오는 코드
    private fun setBundle(){

        // 소셜로그인 토근 제거시 사용
        val arguments = arguments

        if (arguments != null && arguments.containsKey("loginType")) {

            when (arguments.getString("loginType").toString()) {

                "kakao" -> UserApiClient.instance.unlink { error ->

                    Log.d("TAG", "setIsWithdrawalSuccess: $error")

                }

                "naver" -> startNaverDeleteToken()

                else -> {}

            }

        } else {

            Log.d("TAG", "No arguments provided")

        }

    } // setBundle()


    // Observe 관련 메서드
    private fun setObserve() {

        // 로그인 버튼 클릭 했을때
        loginViewModel.isSuccessLogin.observe(viewLifecycleOwner) {

            when (it) {

                "success" -> {

                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                }

                "failure" -> Toast.makeText(requireContext(),
                    getString(R.string.cannotLogin), Toast.LENGTH_SHORT).show()

                "disconnect" -> Toast.makeText(requireContext(),
                    getString(R.string.networkConnectionIsUnstable), Toast.LENGTH_SHORT).show()

                "duplicatedId" -> Toast.makeText(requireContext(), getString(R.string.alreadyExistEmail), Toast.LENGTH_SHORT).show()

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

                if(throwable != null){ // 오류가 있으면 웹으로 콜백

                    UserApiClient.instance.loginWithKakaoAccount(requireContext(),
                        callback = loginViewModel.emailLoginCallback)

                }else{ // 오류가 없으면 카카오 앱으로 로그인

                    UserApiClient.instance.me { user, error ->

                        if(error != null){

                            Log.d("TAG", "requestKaKaoLogin: 로그인 실패")

                        }else{

                            /**서버에 로그인 또는 회원가입 하는 요청 필요**/
                            loginViewModel.socialLogin(user?.kakaoAccount?.email.toString(),"kakao")

                        }

                    }

                }


            }
        } else {

            // 카카오톡이 설치되어 있지 않다면 계정 로그인
            UserApiClient.instance.loginWithKakaoAccount(requireContext(),
                callback = loginViewModel.emailLoginCallback) // 카카오 이메일 로그인

        }

    } // setKaKaoLogin()


    // 네이버 로그인
    private fun requestNaverLogin() {

        NaverIdLoginSDK.logout()

        NaverIdLoginSDK.authenticate(activity as MainActivity,loginViewModel.requestNaverLogin())

    } // startNaverLogin()


    /** 네이버 아이디 토큰 삭제 **/
    private fun startNaverDeleteToken(){

        NidOAuthLogin().callDeleteTokenApi(requireContext(), object : OAuthLoginCallback {

            override fun onSuccess() {}

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