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
        setBackPressed()
    }


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

                R.id.payFragment -> {

                    navController.popBackStack()
                    binding.bottomNavigationView.visibility = View.VISIBLE

                }

            }

        }

    } // setBackPressed()


}