package com.example.fitfit.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentFindPasswordBinding
import com.example.fitfit.viewModel.FindPasswordViewModel


class FindPasswordFragment : Fragment() {

    val TAG = "비밀번호 재설정 프래그먼트"

    private lateinit var binding: FragmentFindPasswordBinding
    private lateinit var findPasswordViewModel: FindPasswordViewModel
    private lateinit var mode:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_password, container, false)

        setVariable()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserve()
        setListener()
    }



    // 변수 초기화
    private fun setVariable() {

        binding.lifecycleOwner = this

        findPasswordViewModel = FindPasswordViewModel()
        binding.findPasswordViewModel = findPasswordViewModel
        
        findPasswordViewModel.setMode(requireArguments().getString("mode").toString())

    } // setVariable



    //리스너 세팅
    private fun setListener(){


       binding.editTextEmail.addTextChangedListener {

           //쉐어드의 유저 아이디와 입력 아이디가 일치하면 버튼 활성화
           when(findPasswordViewModel.isEmailCorrect(it.toString())){
               true ->   {
                   binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
                   binding.buttonNext.isEnabled = true
                   binding.textViewEmailCorrect.text = getString(R.string.correctUserEmail)
                   binding.textViewEmailCorrect.setTextColor(ContextCompat.getColor(requireContext(),R.color.personal))
               }
               false -> {
                   binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
                   binding.buttonNext.isEnabled = false
                   binding.textViewEmailCorrect.text = getString(R.string.inCorrectUserEmail)
                   binding.textViewEmailCorrect.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
               }
           }

       }

        //에딧텍스트 포커스 체인지 리스너
        binding.editTextEmail.setOnFocusChangeListener { view, hasFocus ->
            when(hasFocus){
                true ->  view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
                false ->  view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
            }
        }

    } // setListener()



    // Observe 관련 메서드
    private fun setObserve() {

        findPasswordViewModel.mode.observe(viewLifecycleOwner){
            when(it){
                "passwordChange" -> {
                    binding.linearLayout1.visibility = View.VISIBLE
                    binding.linearLayout2.visibility = View.GONE
                }
                "passwordReset" -> {
                    binding.linearLayout1.visibility = View.VISIBLE
                    binding.linearLayout2.visibility = View.GONE
                }
                "emailVerification" -> {
                    binding.linearLayoutCode.visibility = View.VISIBLE
                }
                "password" -> {
                    binding.linearLayout1.visibility = View.GONE
                    binding.linearLayout2.visibility = View.VISIBLE
                }
                "complete" -> {
                    binding.linearLayout2.visibility = View.GONE
                    binding.linearLayout3.visibility = View.VISIBLE
                }
            }
        }

    } // setObserve()
    
}