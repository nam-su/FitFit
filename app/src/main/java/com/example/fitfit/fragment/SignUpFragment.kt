package com.example.fitfit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        setListener()

        return binding.root

    }

    //리스너 세팅
    fun setListener(){

        //editTextEmail 포커스 리스너
       binding.editTextEmail.setOnFocusChangeListener { v, hasFocus ->
           if (hasFocus) {
               // 포커스가 활성화 된 상태
               binding.editTextEmail.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
           } else {
               // 포커스가 비활성화 된 상태
               binding.editTextEmail.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
           }
       }

        //editTextCode 포커스 리스너
        binding.editTextCode.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // 포커스가 활성화 된 상태
                binding.editTextCode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
            } else {
                // 포커스가 비활성화 된 상태
                binding.editTextCode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
            }
        }

    } // setListener()

}