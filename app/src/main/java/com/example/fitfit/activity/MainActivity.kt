package com.example.fitfit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavOptions

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.fitfit.R
import com.example.fitfit.databinding.ActivityMainBinding
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private val TAG = "메인액티비티"

    private lateinit var binding: ActivityMainBinding

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController

    // onCreate
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setView()

    } // onCreate()


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

        // 바인딩 라이프사이클 오너 초기화
        binding.lifecycleOwner = this

        // 프래그먼트 백스택 관찰을 위한 리스너
        /** 마지막 작업 후 삭제 **/
        navHostFragment.childFragmentManager.addOnBackStackChangedListener {

            Log.d(TAG, "setView: 백스택 : " + navHostFragment.childFragmentManager.backStackEntryCount.toString())

        }

    } // setView()


    // 바텀 내비 visible 메서드
    fun visibleBottomNavi() {

        binding.bottomNavigationView.visibility = View.VISIBLE

    } // visibleBottomNavi()


    // 바텀내비 gone 메서드
    fun goneBottomNavi() {

        binding.bottomNavigationView.visibility = View.GONE

    } // goneBottomNavi()

}