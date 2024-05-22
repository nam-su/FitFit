package com.example.fitfit.fragment

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
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

        //에딧텍스트 이메일 포커스 체인지 리스너
        binding.editTextEmail.setOnFocusChangeListener { _, hasFocus ->
            signUpViewModel.onEmailFocusChanged(hasFocus)
        }
        
        //에딧텍스트 텍스트 체인지 리스너
        binding.editTextEmail.addTextChangedListener { text ->
            text?.let { signUpViewModel.validateEmail(it.toString()) }
        }


        //에딧텍스트 코드 포커스 체인지 리스너
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
        signUpViewModel.isEmailFocus.observe(viewLifecycleOwner) {
            if (it) {
                binding.editTextEmail.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
            } else {
                binding.editTextEmail.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
            }
        }

        //에딧텍스트 전송코드 관찰
        signUpViewModel.isCodeFocus.observe(viewLifecycleOwner) {
            if (it) {
                binding.editTextCode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
            } else {
                binding.editTextCode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
            }
        }

        //이메일 유효성 관찰
        signUpViewModel.isEmailValid.observe(viewLifecycleOwner){
            if(it) {
                binding.buttonNext.isEnabled = true
                binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
                binding.textViewValid.visibility = View.VISIBLE
                binding.textViewInValid.visibility = View.GONE
            }else {
                binding.buttonNext.isEnabled = false
                binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
                binding.linearLayoutCode.visibility = View.GONE
                binding.textViewValid.visibility = View.GONE
                binding.textViewInValid.visibility = View.VISIBLE
            }

            //코드 유효성 관찰
            signUpViewModel.isCodeValid.observe(viewLifecycleOwner){
                if(it){
                    Toast.makeText(requireContext(), "유효한 인증코드 입니다.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "유효하지 않은 인증코드 입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //사용 가능 이메일 관찰
        signUpViewModel.isEmailPossible.observe(viewLifecycleOwner){
            if (it){
                binding.linearLayoutCode.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "사용가능한 이메일 입니다.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "사용할 수 없는 이메일 입니다.", Toast.LENGTH_SHORT).show()
            }
        }


        //이메일 전송 관찰
        signUpViewModel.isEmailSend.observe(viewLifecycleOwner){
            Log.d(TAG, "setObserve: ${it.toString()}")
            if (it){
                Toast.makeText(requireContext(), "인증코드를 전송 했습니다.", Toast.LENGTH_SHORT).show()
                binding.buttonNext.isEnabled = true
                binding.buttonSend.text = "재전송"
            }else{
                Toast.makeText(requireContext(), "전송에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    } // setObserve()


}