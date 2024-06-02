package com.example.fitfit.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentUserBinding
import com.example.fitfit.viewModel.HomeViewModel
import com.example.fitfit.viewModel.UserViewModel
import kotlinx.coroutines.coroutineScope
import kotlin.math.log

class UserFragment : Fragment() {

    private val TAG = "유저 프래그먼트"

    lateinit var binding: FragmentUserBinding
    lateinit var userViewModel: UserViewModel


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setListener()
        setObserve()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        userViewModel = UserViewModel()
        binding.userViewModel = userViewModel

        userViewModel.setUserInformation()



    } // setVariable()



    //setListener(){
    private fun setListener(){

        //설정 버튼 누르기
        binding.imageButtonSetting.setOnClickListener {
           binding.drawerLayout.openDrawer(GravityCompat.END)
        }


        // NavigationView의 아이템 선택 리스너 설정
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            userViewModel.selectItem(menuItem.itemId)
            true
        }
    }



    //setObserve()
    private fun setObserve(){

        //메뉴 선택에 대한 리스너
        userViewModel.selectedMenuItem.observe(viewLifecycleOwner){ setSelectedMenuItem(it) }

    }


    
    //drawerLayout에 체크된 메뉴아이템 관찰 대한 처리
    private fun setSelectedMenuItem(it:Int){
        //모든 메뉴 아이템의 체크 상태를 false로 설정
        for (i in 0 until binding.navigationView.menu.size()) {
            val item = binding.navigationView.menu.getItem(i)
            item.isChecked = false
        }

        //네비게이션 메뉴 선택 효과 주기
        //isChecked = true : 메뉴 텍스트 굵기가 굵어짐.
        binding.navigationView.menu.findItem(it).isChecked = true

        Log.d(TAG, "setObserve: $it")
        when(it){
            binding.navigationView.menu.getItem(0).itemId   -> {
                //프로필 수정 프래그먼트로 이동
                Log.d(TAG, "setObserve: 프로필 수정 프래그먼트로 이동")
            }
            binding.navigationView.menu.getItem(1).itemId   -> {
                //비밀번호 변경 프래그먼트로 이동
                Log.d(TAG, "setObserve: 비밀번호 변경 프래그먼트로 이동")
            }
            binding.navigationView.menu.getItem(2).itemId   -> {
                //로그아웃 다이얼로그
                Log.d(TAG, "setObserve: 로그아웃 다이얼로그")
            }
            binding.navigationView.menu.getItem(10).itemId   -> {
                //회원탈퇴 다이얼로그
                Log.d(TAG, "setObserve: 회원탈퇴 다이얼로그")
            }
            else -> {
                Log.d(TAG, "setObserve: 빈칸고름")
            }
        }
    }
}