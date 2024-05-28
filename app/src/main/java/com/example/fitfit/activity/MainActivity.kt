package com.example.fitfit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setView()
        setVariable()
        setBackPressed()
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


        // 프래그먼트 백스택 관찰을 위한 리스너
        navHostFragment.childFragmentManager.addOnBackStackChangedListener {

            Log.d(TAG, "setView: 백스택 : " + navHostFragment.childFragmentManager.backStackEntryCount.toString())
        }

    } // setView()


    // 변수 초기화
    private fun setVariable() {

        binding.lifecycleOwner = this

    } // setVariable()


    // 스플래시 시작
    private fun startSplash() {

        // 바텀네비게이션 view gone 해놓고 스플래시 프래그먼트로 진입
        binding.bottomNavigationView.visibility = View.GONE

    } // startSplash()


    // 로그인 했을때 홈 프래그먼트로 전환.
    fun changeNavHostFragment() {

        navController.navigate(R.id.action_loginFragment_to_homeFragment)
        binding.bottomNavigationView.visibility = View.VISIBLE

    } // changeNavHostFragment()



    // 로그인 했을때 홈 프래그먼트로 전환.
    fun changeSignUpFragment() {

        navController.navigate(R.id.action_loginFragment_to_signUpFragment)

    } // changeSignUpFragment()



    // 회원가입 완료 되었을 때 로그인 프래그먼트로 전환
    // 로그인 했을때 홈 프래그먼트로 전환.
    fun changeSignUpToLoginFragment() {

        navController.navigate(R.id.action_signUpFragment_to_loginFragment)

    } // changeSignUpToLoginFragment()

    // 스플래시에서 로그인 기록 없으면 로그인 프래그먼트로 전환
    fun changeSplashToLoginFragment() {

        navController.navigate(R.id.action_splashFragment_to_loginFragment)

    } // changeSplashToLoginFragment()


    // 스플래시에서 로그인 기록 있으면 홈 프래그먼트로 전환
    fun changeSplashToHomeFragment() {

        navController.navigate(R.id.action_splashFragment_to_homeFragment)
        binding.bottomNavigationView.visibility = View.VISIBLE

    } // changeSplashToHomeFragment()


    // 운동 프래그먼트에서 운동 선택 프래그먼트로 전환.
    fun changeExerciseToExerciseChoiceFragment() {

        navController.navigate(R.id.action_exerciseFragment_to_exerciseChoiceFragment)
        binding.bottomNavigationView.visibility = View.GONE

    } // changeExerciseToPoseDetectionFragment()


    // 운동선택 프래그먼트에서 동작 인식 프래그먼트로 전환.
    fun changeExerciseChoiceToPoseDetectionFragment() {

        navController.navigate(R.id.action_exerciseChoiceFragment_to_poseDetectionFragment)

    } // changeExerciseChoiceToPoseDetectionFragment()


    //뒤로가기 버튼 클릭시
    private fun setBackPressed() {

        onBackPressedDispatcher.addCallback(this){

            // R.id.fragment -> fragment에서 백버튼 눌렀을 때에대한 처리
            when(navController.currentDestination?.id){

                R.id.loginFragment -> finish()
                R.id.homeFragment -> finish()
                R.id.exerciseFragment -> finish()
                R.id.diaryFragment -> finish()
                R.id.userFragment -> finish()
                R.id.exerciseChoiceFragment -> {

                    navController.popBackStack()
                    binding.bottomNavigationView.visibility = View.VISIBLE

                }
                R.id.poseDetectionFragment -> {

                    navController.popBackStack()

                }

            }

        }

    } // setBackPressed()


}