package com.example.fitfit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.fitfit.R
import com.example.fitfit.databinding.ActivityMainBinding

import com.example.fitfit.fragment.HomeFragment
import com.example.fitfit.fragment.LoginFragment
import com.example.fitfit.fragment.SplashFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TAG = "메인액티비티"

    private lateinit var binding: ActivityMainBinding


    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController

    private val splashFragment = SplashFragment()
    private val loginFragment = LoginFragment()
    private val homeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setView()
        setVariable()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        startSplash()
    } // onStart()


    // 뷰관련 초기화
    private fun setView() {

        // 시작할때 상태바색 바꾸기.
        window.statusBarColor = ContextCompat.getColor(this, R.color.personal)

        // 네비게이션을 담는 호스트
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // 네비게이션 컨트롤러
        navController = navHostFragment.navController

        // 바텀 네비게이션 뷰와 네비게이션을 묶는다.
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        // 기본 아이콘 색상 적용 안함
        binding.bottomNavigationView.itemIconTintList = null


    } // setView()


    // 변수 초기화
    private fun setVariable() {

        binding.lifecycleOwner = this

    } // setVariable()


    // 스플래시 시작
    private fun startSplash() {

        // 바텀네비게이션 view gone 해놓고 스플래시 프래그먼트로 진입
        binding.bottomNavigationView.visibility = View.GONE

        // UI 변화 및 3초 딜레이 주기 위해서 코루틴 사용
        CoroutineScope(Dispatchers.Main).launch {

            delay(3000)

            navController.navigate(R.id.action_splashFragment_to_loginFragment)

        }

    } // startSplash()


    // 로그인 했을때 홈 프래그먼트로 전환.
    fun changeNavHostFragment() {

        navController.navigate(R.id.action_loginFragment_to_homeFragment)
        binding.bottomNavigationView.visibility = View.VISIBLE

    } // changeNavHostFragment()


    // 로그인 했을때 홈 프래그먼트로 전환.
    fun changeSignUpFragment() {

        navController.navigate(R.id.action_loginFragment_to_signUpFragment)

    } // changeNavHostFragment()

}