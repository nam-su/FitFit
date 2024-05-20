package com.example.fitfit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.fitfit.R
import com.example.fitfit.databinding.ActivityMainBinding
import com.example.fitfit.fragment.LoginFragment
import com.example.fitfit.fragment.SplashFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    private val splashFragment = SplashFragment()
    private val loginFragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        setView()
        setVariable()

    }

    override fun onStart() {
        super.onStart()
        startSplash()
    } // onStart()


    // 뷰관련 초기화 메서드
    private fun setView() {

        // 네비게이션을 담는 호스트
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // 네비게이션 컨트롤러
        val navController = navHostFragment.navController

        // 바텀 네비게이션 뷰와 네비게이션을 묶는다.
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)

        // 기본 아이콘 색상 적용 안함
        binding.bottomNavigationView.itemIconTintList = null

    } // setView()

    private fun setVariable() {

        binding.lifecycleOwner = this
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()

    }


    // 스플래시 시작하는 메서드
    private fun startSplash() {

        binding.bottomNavigationView.visibility = View.GONE

        // UI 변화 및 3초 딜레이 주기 위해서 코루틴 사용
        CoroutineScope(Dispatchers.Main).launch {

            fragmentTransaction.replace(R.id.linearLayoutMainLayout,splashFragment)
            fragmentTransaction.commit()

            delay(3000)

            // transaction 초기화 후 replace 로 다른 프래그먼트를 띄워준다.
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.constraintLayoutSplash,loginFragment)
            fragmentTransaction.commit()

        }

    } // startSplash()


    // 로그인 성공했을 때에 대한 간단한 화면전환 메서드
    fun successLogin() {

        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(loginFragment)
        fragmentTransaction.remove(splashFragment)
        fragmentTransaction.commit()
        binding.bottomNavigationView.visibility = View.VISIBLE

    } // successLogin()

}