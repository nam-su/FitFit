package com.example.fitfit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentLoginBinding
import com.example.fitfit.viewModel.LoginViewModel

class LoginFragment() : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)

        return binding.root

    } // onCreateView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setClickListener()

    } // onViewCreated()

    // 변수 초기화
    private fun setVariable() {

        viewModel = LoginViewModel()

    } // setVariable


    // 클릭리스너 초기화
    private fun setClickListener() {

        binding.buttonLogin.setOnClickListener {

            (activity as MainActivity).successLogin()

        }

    } // setClickListener()

}