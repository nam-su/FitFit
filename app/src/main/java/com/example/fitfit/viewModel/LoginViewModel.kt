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

    val TAG = "로그인 뷰모델"

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

            Log.d(TAG, "login: ${response.message()}")
            Log.d(TAG, "login: ${response.isSuccessful}")
            Log.d(TAG, "login: ${response.body()}")
//            Log.d(TAG, "login: ${response.body()?.myChallengeList}")



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

                Log.d(TAG, "login: ${response.message()}")
                Log.d(TAG, "login: ${response.isSuccessful}")
                Log.d(TAG, "login: ${response.body()}")
                _isSuccessLogin.value = "disconnect"

            }

        }

    } // login()


    // 로그인 성공했을때 Shared에 데이터 추가해준다.
    private fun setSharedPreferencesUserinfo(user: User) {

        _isSuccessLogin.value = "success"
        loginModel.setSharedPreferencesUserInfo(user)

    } // setSharedPreferencesUserInfo()


    // 카카오톡 어플이 있는 경우 카카오톡 로그인 요청
    fun requestKakaoApplicationLogin(token: OAuthToken?, error: Throwable?): String {

        val result = ""

        // 로그인 성공 부분
        if (token != null) {

            return "successLogin"

        }

        // 로그인 실패 부분
        else if (error != null) {

            Log.e(TAG, "로그인 실패 $error")

            // 사용자가 취소
            return if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {

                "errorUserCancel"

            }
            // 다른 오류

            else {

                "elseError"// 카카오 이메일 로그인

            }

        }

        return result

    } // requestKakaoApplicationLogin()


    // 카카오 이메일 로그인 콜백
    // 카카오 이메일 로그인 콜백
    val emailLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->

        Log.d(TAG, "이메일 로그인 콜백: ")

        if (error != null) {

            Log.e(TAG, "로그인 실패 $error")

        } else if (token != null) {

            Log.e(TAG, "로그인 성공 ${token.accessToken}")
            UserApiClient.instance.me { user, error ->
                if(error != null) {

                    Log.d(TAG, "유저정보 읽어오기 실패: ")

                } else {

                    Log.d(TAG, "유저 아이디 : ${user?.id}")
                    Log.d(TAG, "유저 이메일 : ${user?.kakaoAccount?.email}")

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

                Log.d(TAG, "onSuccess:id: $userId \\ntoken: $naverToken")
                Log.d(TAG, "onSuccess: 이메일 : $userEmail")

            }

            override fun onFailure(httpStatus: Int, message: String) {

                val errorCode = NaverIdLoginSDK.getLastErrorCode().code

                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

                Log.d(TAG, "onFailure:  ${errorCode}\\n\" +\n" +"\"errorDescription: ${errorDescription}\"")

            }

            override fun onError(errorCode: Int, message: String) {

                onFailure(errorCode, message)

            }

        }

        /** OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. */
        val oauthLoginCallback = object : OAuthLoginCallback {

            override fun onSuccess() {

                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                Log.d(TAG, "onSuccess: 로그인 인증이 성공 했을 때")

                naverToken = NaverIdLoginSDK.getAccessToken()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)

            }

            override fun onFailure(httpStatus: Int, message: String) {

                val errorCode = NaverIdLoginSDK.getLastErrorCode().code

                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

                Log.d(TAG, "onFailure:  ${errorCode}\\n\" +\n" +"\"errorDescription: ${errorDescription}\"")

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

            Log.d(TAG, "로그인 성공: ${account.email}")

            _googleLoginResult.value = "success"

        } catch (e: ApiException) {

            Log.e(TAG, "로그인 실패: ${e.statusCode}", e)

            _googleLoginResult.value = "failure"

        }

    } // handleGoogleLoginResult()

}