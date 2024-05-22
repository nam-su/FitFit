package com.example.fitfit.fragment

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentLoginBinding
import com.example.fitfit.databinding.FragmentSignUpBinding
import com.example.fitfit.viewModel.LoginViewModel
import com.example.fitfit.viewModel.SignUpViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sign


class SignUpFragment : Fragment() {

    val TAG = "회원가입 프래그먼트"

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setObserve()
        setListener()
    }



    // 변수 초기화
    private fun setVariable() {

        binding.lifecycleOwner = this

        signUpViewModel = SignUpViewModel()
        binding.signUpViewModel = signUpViewModel

    } // setVariable



    //리스너 세팅
    fun setListener(){

        binding.editTextEmail.setOnFocusChangeListener { _, hasFocus ->
            signUpViewModel.onEmailFocusChanged(hasFocus)
        }

        binding.editTextCode.setOnFocusChangeListener { _, hasFocus ->
            signUpViewModel.onCodeFocusChanged(hasFocus)
        }

    } // setListener()



    // Observe 관련 메서드
    private fun setObserve() {

        //페이지 카운트 observe
        signUpViewModel.pageCount.observe(viewLifecycleOwner) {

            Log.d(TAG, "setObserve: ${signUpViewModel.pageCount.value}")
            when(it) {

                1 -> {
                    binding.linearLayout1.visibility = View.VISIBLE
                    binding.linearLayout2.visibility = View.GONE
                    binding.linearLayout3.visibility = View.GONE
                }

                2 -> {
                    binding.linearLayout1.visibility = View.GONE
                    binding.linearLayout2.visibility = View.VISIBLE
                    binding.linearLayout3.visibility = View.GONE
                }

                3 -> {
                    binding.linearLayout1.visibility = View.GONE
                    binding.linearLayout2.visibility = View.GONE
                    binding.linearLayout3.visibility = View.VISIBLE
                }

            }

        }


        //에딧텍스트 이메일 관찰
        signUpViewModel.emailFocus.observe(viewLifecycleOwner) {
            if (it) {
                binding.editTextEmail.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
            } else {
                binding.editTextEmail.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
            }
        }

        //에딧텍스트 전송코드 관찰
        signUpViewModel.codeFocus.observe(viewLifecycleOwner) {
            if (it) {
                binding.editTextCode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
            } else {
                binding.editTextCode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
            }
        }

    } // setObserve()

}