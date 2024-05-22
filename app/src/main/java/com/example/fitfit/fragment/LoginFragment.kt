package com.example.fitfit.fragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentLoginBinding
import com.example.fitfit.viewModel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment() : Fragment() {

    val TAG = "로그인 프래그먼트"

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setClickListener()
        setObserve()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = loginViewModel


    } // setVariable


    // 클릭리스너 초기화
    private fun setClickListener() {

    } // setClickListener()


    // Observe 관련 메서드
    private fun setObserve() {

        loginViewModel.isSuccessLogin.observe(viewLifecycleOwner) {

            Log.d(TAG, "setClickListener: 버튼 눌렀을때 isSuccess" + loginViewModel.isSuccessLogin.value)

            when(it) {

                true -> CoroutineScope(Dispatchers.Main).launch {(activity as MainActivity).changeNavHostFragment()}

                false -> Toast.makeText(activity,"아이디 혹은 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()

            }

        }

    } // setObserve()

}