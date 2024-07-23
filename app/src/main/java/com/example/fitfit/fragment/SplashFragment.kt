package com.example.fitfit.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.CustomDialogNetworkDisconnectBinding
import com.example.fitfit.databinding.CustomDialogTwoButtonBinding
import com.example.fitfit.databinding.FragmentSplashBinding
import com.example.fitfit.function.MyApplication
import com.example.fitfit.viewModel.SplashViewModel
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {
    
    private lateinit var binding: FragmentSplashBinding
    private lateinit var splashViewModel: SplashViewModel

    lateinit var customDialogBinding: CustomDialogNetworkDisconnectBinding

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_splash,container,false)
        customDialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_network_disconnect, null, false)

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

        if (!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

            view?.postDelayed({

                setCustomDialog()

            },1500)

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


    //커스텀 다이얼로그 띄우기
    private fun setCustomDialog(){

        // 부모가 있는지 확인하고, 있다면 부모에서 제거
        customDialogBinding.root.parent?.let {
            (it as ViewGroup).removeView(customDialogBinding.root)
        }

        //다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(customDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customDialogBinding.textViewButtonOk.setOnClickListener {

            (activity as MainActivity).finish()

        }

        dialog.setOnCancelListener {

            (activity as MainActivity).finish()

        }

        dialog.show()

    } // setCustomDialog()

}