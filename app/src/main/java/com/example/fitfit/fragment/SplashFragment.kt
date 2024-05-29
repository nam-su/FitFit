package com.example.fitfit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentSplashBinding
import com.example.fitfit.viewModel.SplashViewModel

class SplashFragment : Fragment() {
    
    private lateinit var binding: FragmentSplashBinding
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_splash,container,false)

        return binding.root

    } // onCreateView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setObserve()
        splashViewModel.checkLogin()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        splashViewModel = SplashViewModel()
        (activity as MainActivity).goneBottomNavi()

    } // setVariable()


    // Observe 관련 메서드
    private fun setObserve() {

        // 쉐어드에 유저데이터가 있는 경우 홈으로 바로 진입.
        splashViewModel.isCheckLogin.observe(viewLifecycleOwner) {

            when(it) {

                true -> {

                    this.findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    (activity as MainActivity).visibleBottomNavi()

                }

                else -> this.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

            }

        }

    } // setObserve()


}