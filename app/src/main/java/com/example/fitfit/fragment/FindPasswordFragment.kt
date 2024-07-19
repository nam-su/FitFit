package com.example.fitfit.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentFindPasswordBinding
import com.example.fitfit.viewModel.FindPasswordViewModel


class FindPasswordFragment : Fragment() {

    val TAG = "비밀번호 재설정 프래그먼트"

    private lateinit var binding: FragmentFindPasswordBinding
    private lateinit var findPasswordViewModel: FindPasswordViewModel


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_password, container, false)

        setVariable()

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserve()
        setListener()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        binding.lifecycleOwner = this

        findPasswordViewModel = FindPasswordViewModel()

        Log.d(TAG, "setVariable: ${requireArguments().getString("startingPoint").toString()}")

        binding.findPasswordViewModel = findPasswordViewModel

        findPasswordViewModel.setStartingPoint(requireArguments().getString("startingPoint").toString())

    } // setVariable


    // 리스너 초기화
    private fun setListener() {

        // 완료 버튼 클릭
        binding.buttonComplete.setOnClickListener {

            this.findNavController().navigate(R.id.action_findPasswordFragment_to_splashFragment)

            Toast.makeText(requireContext(), "회원정보가 변경되었습니다. 다시 로그인 해 주세요.", Toast.LENGTH_SHORT).show()

        }

        binding.editTextEmail.addTextChangedListener {

            findPasswordViewModel.validationEmail(it.toString())

        }

        // 에딧텍스트 이메일 포커스 체인지 리스너
        binding.editTextEmail.setOnFocusChangeListener { view, hasFocus ->

            when (hasFocus) {

                true -> view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)

                false -> view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)

            }

        }

        // 에딧텍스트 패스워드 포커스 체인지 리스너
        binding.editTextPassword.setOnFocusChangeListener { view, hasFocus ->

            when (hasFocus) {

                true -> view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)

                false -> view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)

            }

        }

        // 에딧텍스트 패스워드 재확인 포커스 체인지 리스너
        binding.editTextReconfirmPassword.setOnFocusChangeListener { view, hasFocus ->

            when (hasFocus) {

                true -> view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)

                false -> view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)

            }

        }

        // editTextPassword 텍스트 체인지 리스너
        binding.editTextPassword.addTextChangedListener { text ->

            text?.let {

                if (text.toString() != "") {

                    findPasswordViewModel.validationPassword(it.toString())

                }

            }

        }

        // editTextReConfirmPassword 텍스트 체인지 리스너
        binding.editTextReconfirmPassword.addTextChangedListener { text ->

            text?.let {

                if (text.toString() != "") {

                    findPasswordViewModel.correctPassword(binding.editTextPassword.text.toString(), it.toString())

                }

            }

        }

    } // setListener()


    // Observe 관련 메서드
    private fun setObserve() {

        // 모드 관찰
        findPasswordViewModel.mode.observe(viewLifecycleOwner) {

            when (it) {

                "emailCheck" -> {

                    binding.linearLayout1.visibility = View.VISIBLE
                    binding.linearLayout2.visibility = View.GONE
                    binding.linearLayoutCode.visibility = View.GONE
                    binding.editTextEmail.setText("")

                }

                "codeCheck" -> {

                    binding.linearLayoutCode.visibility = View.VISIBLE
                    binding.editTextCode.setText("")

                }

                "passwordChange" -> {

                    binding.editTextPassword.setText("")
                    binding.editTextReconfirmPassword.setText("")
                    binding.linearLayout1.visibility = View.GONE
                    binding.linearLayout2.visibility = View.VISIBLE
                    binding.linearLayout3.visibility = View.GONE
                    binding.buttonComplete.visibility = View.GONE
                    binding.buttonNext.visibility = View.VISIBLE
                    binding.editTextReconfirmPassword.visibility = View.GONE
                    binding.textViewPasswordCorrect.visibility = View.GONE
                    binding.textViewPasswordValid.visibility = View.GONE

                }

                "complete" -> {

                    binding.imageViewBack.visibility = View.INVISIBLE
                    binding.linearLayout2.visibility = View.GONE
                    binding.linearLayout3.visibility = View.VISIBLE
                    binding.buttonComplete.visibility = View.VISIBLE
                    binding.buttonNext.visibility = View.GONE

                }

                "close" -> {

                    this.findNavController().popBackStack()

                    if (findPasswordViewModel.startingPoint.value == "loginFragment") {

                        (activity as MainActivity).goneBottomNavi()

                    } else if (findPasswordViewModel.startingPoint.value == "userFragment") {

                        (activity as MainActivity).visibleBottomNavi()

                    }

                }

            }

        }

        // 이메일 유효성 관찰
        findPasswordViewModel.isEmailValid.observe(viewLifecycleOwner) {

            setEmailValid(it)

        }

        // 사용 가능 이메일 관찰
        findPasswordViewModel.isEmailPossible.observe(viewLifecycleOwner) {

            when (it) {

                true -> {

                    binding.linearLayoutCode.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "사용가능한 이메일 입니다.", Toast.LENGTH_SHORT).show()

                }

                false -> Toast.makeText(requireContext(), "이미 존재하는 이메일 입니다.", Toast.LENGTH_SHORT).show()

            }

        }

        // 이메일 전송 관찰
        findPasswordViewModel.isEmailSend.observe(viewLifecycleOwner) {

            when (it) {

                true -> {

                    Toast.makeText(requireContext(), "인증코드를 전송 했습니다.", Toast.LENGTH_SHORT).show()
                    binding.buttonNext.isEnabled = true

                }

                false -> Toast.makeText(requireContext(), "전송에 실패했습니다.", Toast.LENGTH_SHORT).show()

            }

        }

        // 패스워드 유효성 관찰
        findPasswordViewModel.isPasswordValid.observe(viewLifecycleOwner) { setPasswordValid(it) }

        // 패스워드 일치 여부 관찰
        findPasswordViewModel.isPasswordCorrect.observe(viewLifecycleOwner) { setPasswordCorrect(it) }

        // 코드 유효성 관찰
        findPasswordViewModel.isCodeValid.observe(viewLifecycleOwner) {

            when (it) {

                true -> Toast.makeText(requireContext(), getString(R.string.code_verification_successful), Toast.LENGTH_SHORT).show()

                false -> Toast.makeText(requireContext(), getString(R.string.code_verification_failure),Toast.LENGTH_SHORT).show()

            }

        }

        //이메일 유효성 관찰
        findPasswordViewModel.isEmailValid.observe(viewLifecycleOwner) { setEmailValid(it) }

    } // setObserve()


    //패스워드 유효성 관찰에 대한 처리
    private fun setPasswordValid(it: Boolean) {

        when (it) {

            true -> {

                binding.editTextReconfirmPassword.visibility = View.VISIBLE
                binding.textViewPasswordValid.text = getString(R.string.validPasswordFormat)
                binding.textViewPasswordValid.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))

            }

            false -> {

                binding.editTextReconfirmPassword.visibility = View.GONE
                binding.textViewPasswordCorrect.visibility = View.GONE
                binding.textViewPasswordValid.visibility = View.VISIBLE
                binding.textViewPasswordValid.text = getString(R.string.inValidPasswordFormat)
                binding.textViewPasswordValid.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

            }

        }

    } // setPasswordValid()


    // 패스워드 일치 관찰에 대한 처리
    private fun setPasswordCorrect(it: Boolean) {

        when (it) {

            true -> {

                if (binding.editTextPassword.text.toString() != "") {

                    binding.buttonNext.isEnabled = true
                    binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
                    binding.textViewPasswordCorrect.visibility = View.VISIBLE
                    binding.textViewPasswordCorrect.text = getString(R.string.correctPasswordFormat)
                    binding.textViewPasswordCorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))

                }

            }

            false -> {

                binding.buttonNext.isEnabled = false
                binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
                binding.textViewPasswordCorrect.visibility = View.VISIBLE
                binding.textViewPasswordCorrect.text = getString(R.string.inCorrectPasswordFormat)
                binding.textViewPasswordCorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

            }

        }

    } // setPasswordValid()


    //이메일 유효성 관찰에 대한 처리
    private fun setEmailValid(it: Boolean) {

        when (it) {

            true -> {

                binding.buttonNext.isEnabled = true
                binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
                binding.textViewEmailCorrect.visibility = View.VISIBLE
                binding.textViewEmailCorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))
                binding.textViewEmailCorrect.text = getString(R.string.validEmailFormat)

            }

            false -> {

                binding.buttonNext.isEnabled = false
                binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
                binding.linearLayoutCode.visibility = View.GONE
                binding.textViewEmailCorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.textViewEmailCorrect.text = getString(R.string.inValidEmailFormat)

            }

        }

    } // setEmailValid()

}