package com.example.fitfit.viewModel

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitfit.data.User
import com.example.fitfit.model.LoginModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val loginModel = LoginModel()

    private val _isSuccessLogin = MutableLiveData<String>()

    val isSuccessLogin: LiveData<String>
        get() = _isSuccessLogin

    private val _googleLoginResult = MutableLiveData<String>()

    val googleLoginResult: LiveData<String>
    get() = _googleLoginResult


    // 로그인 메서드
    fun login(id: String,password: String){

        viewModelScope.launch {

            val response = loginModel.login(id,password,"","login")

            if(response.isSuccessful && response.body() != null) {

                val user = response.body()!!

                when(user.result){

                        "failure" -> _isSuccessLogin.value = "failure"

                        else -> {

                            setSharedPreferencesUserinfo(user)

                        }

                }

            // 통신 실패의 경우
            } else {

                _isSuccessLogin.value = "disconnect"

            }

        }

    } // login()


    // 소셜 로그인 메서드
    fun socialLogin(id: String,loginType: String){

        viewModelScope.launch {

            val response = loginModel.socialLogin(id,loginType)

            if(response.isSuccessful && response.body() != null) {

                val user = response.body()!!

                when(user.result){

                    "failure" -> _isSuccessLogin.value = "failure"

                    "duplicatedId" -> _isSuccessLogin.value = "duplicatedId"

                    else -> {

                        setSharedPreferencesUserinfo(user)

                    }

                }

                // 통신 실패의 경우
            } else {

                _isSuccessLogin.value = "disconnect"

            }

        }

    } // login()


    // 로그인 성공했을때 Shared에 데이터 추가해준다.
    private fun setSharedPreferencesUserinfo(user: User) {

        if (loginModel.setSharedPreferencesUserInfo(user)) {

            _isSuccessLogin.value = "success"

        } else {

            _isSuccessLogin.value = "failure"

        }

    } // setSharedPreferencesUserInfo()

    // 카카오 이메일 로그인 콜백
    val emailLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->

        if (error != null) {


        } else if (token != null) {

            UserApiClient.instance.me { user, error ->

                if(error != null) {

                    Log.e("TAG", ": $error")

                } else {

                    socialLogin(user?.kakaoAccount?.email.toString(),"kakao")

                }

            }

        }

    } // emailLoginCallback()



    // 네이버 로그인 요청
    fun requestNaverLogin(): OAuthLoginCallback {

        var naverToken :String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {

            override fun onSuccess(result: NidProfileResponse) {

                val userId = result.profile?.id
                val userEmail = result.profile?.email

                socialLogin(userEmail.toString(), "naver")

            }

            override fun onFailure(httpStatus: Int, message: String) {

                val errorCode = NaverIdLoginSDK.getLastErrorCode().code

                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

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

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)

            }

            override fun onFailure(httpStatus: Int, message: String) {

                val errorCode = NaverIdLoginSDK.getLastErrorCode().code

                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

            }

            override fun onError(errorCode: Int, message: String) {

                onFailure(errorCode, message)

            }

        }

        return oauthLoginCallback

    } // requestNaverLogin()


    // 구글 로그인 요청
    fun requestGoogleLogin(gsc: GoogleSignInClient, launcher: ActivityResultLauncher<Intent>) {

        // 로그아웃 처리
        gsc.signOut()

        val signInIntent = gsc.signInIntent

        launcher.launch(signInIntent)

    } // requestGoogleLogin()


    // 구글 로그인 결과 코드
    fun handleGoogleLoginResult(task: Task<GoogleSignInAccount>) {

        try {

            val account = task.getResult(ApiException::class.java)

            /**
             * 어카운트 계정으로 회원가입 진행
             * account.email과 동일한 email을 갖는 다른 로그인타입이 존재하면 중복
             * account.email이 서버에 존재하지 않으면 회원가입 진행
             * account.email이 서버에 존재하면 로그인 진행 **/

            socialLogin(account.email.toString(),"google")

            _googleLoginResult.value = "success"

        } catch (e: ApiException) {

            _googleLoginResult.value = "failure"

        }

    } // handleGoogleLoginResult()

}