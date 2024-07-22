package com.example.fitfit.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_splash,container,false)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).goneBottomNavi()
        setVariable()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        if (!getNetworkStatus(requireContext())) {

            Toast.makeText(requireContext(), "인터넷 연결이 원활하지 않습니다. \n앱을 종료합니다.", Toast.LENGTH_SHORT).show()
            view?.postDelayed({
                (activity as MainActivity).finish()
            }, 2000) // 2초 후 종료

        } else {

            splashViewModel = SplashViewModel()

            setObserve()

            splashViewModel.checkLogin()

        }

    } // setVariable()


    // Observe 관련 메서드
    private fun setObserve() {

        // 쉐어드에 유저데이터가 있는 경우 홈으로 바로 진입.
        splashViewModel.isCheckLogin.observe(viewLifecycleOwner) {

            when(it) {

                true -> {

                    splashViewModel.selectUserExercise()

                    this.findNavController().navigate(R.id.action_splashFragment_to_homeFragment)

                    (activity as MainActivity).visibleBottomNavi()

                }

                else -> this.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

            }

        }

    } // setObserve()


    //   네트워크 연결 상태를 논리 값으로 반환.
    //   와이파이, 모바일 데이터 연결 중일 경우 true.
    fun getNetworkStatus(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val actNetwork = connectivityManager.getNetworkCapabilities(network)
        return actNetwork?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

}