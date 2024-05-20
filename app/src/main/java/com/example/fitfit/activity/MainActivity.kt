package com.example.fitfit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
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

    private val fragmentManager = supportFragmentManager
    private var fragmentTransaction = fragmentManager.beginTransaction()
    private val splashFragment = SplashFragment()
    private val loginFragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setView()

    }

    override fun onStart() {
        super.onStart()
        startSplash()
    }

    fun setView() {

        // 네비게이션을 담는 호스트
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // 네비게이션 컨트롤러
        val navController = navHostFragment.navController

        // 바텀 네비게이션 뷰와 네비게이션을 묶는다.
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)

        // 기본 아이콘 색상 적용 안함
        binding.bottomNavigationView.itemIconTintList = null

    }

    // 스플래시
    private fun startSplash() {

        CoroutineScope(Dispatchers.Main).launch {

            fragmentTransaction.add(R.id.fragmentSplash,splashFragment)
            fragmentTransaction.commit()

            delay(3000)

            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentSplash,loginFragment)
            fragmentTransaction.commit()

        }

    }

}