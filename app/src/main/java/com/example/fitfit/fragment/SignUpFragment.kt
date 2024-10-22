package com.example.fitfit.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.fitfit.R
import com.example.fitfit.databinding.CustomDialogNetworkDisconnectBinding
import com.example.fitfit.databinding.FragmentSignUpBinding
import com.example.fitfit.function.MyApplication
import com.example.fitfit.viewModel.SignUpViewModel


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel

    lateinit var customNetworkDialogBinding: CustomDialogNetworkDisconnectBinding


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        customNetworkDialogBinding = DataBindingUtil.inflate(inflater,R.layout.custom_dialog_network_disconnect,null,false)

        return binding.root

    } // onCreateVew()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setObserve()
        setListener()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        binding.lifecycleOwner = this

        signUpViewModel = SignUpViewModel()
        binding.signUpViewModel = signUpViewModel

    } // setVariable


    // 리스너 세팅
    private fun setListener(){

        // 다음 버튼 클릭 리스너
        binding.buttonNext.setOnClickListener {

            // 인터넷 연결 x
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            // 인터넷 연결 o
            } else {

                signUpViewModel.setOnButtonNextClick(
                    binding.editTextEmail.text.toString(),
                    binding.editTextCode.text.toString(),
                    binding.editTextPassword.text.toString(),
                    binding.editTextNickname.text.toString()
                )

            }

        }

        // 완료 버튼 클릭 리스너
        binding.buttonComplete.setOnClickListener {

            // 인터넷 연결 x
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            // 인터넷 연결 o
            } else {

                signUpViewModel.setOnButtonCompleteClick(
                    signUpViewModel.signUpEmail.value.toString(),
                    signUpViewModel.signUpPassword.value.toString(),
                    signUpViewModel.signUpNickname.value.toString()
                )

            }

        }

        // 이메일 코드 재전송 버튼
        binding.buttonResend.setOnClickListener {

            // 인터넷 연결 x
            if(!MyApplication.sharedPreferences.getNetworkStatus(requireContext())) {

                setNetworkCustomDialog()

            // 인터넷 연결 o
            } else {

                signUpViewModel.setOnButtonSendClick(binding.editTextEmail.text.toString())

            }

        }

        // 에딧텍스트 이메일 포커스 체인지 리스너
        binding.editTextEmail.setOnFocusChangeListener { _, hasFocus ->

            signUpViewModel.onEmailFocusChanged(hasFocus)

        }

        // editTextEmail 텍스트 체인지 리스너
        binding.editTextEmail.addTextChangedListener { text ->

            text?.let { signUpViewModel.validationEmail(it.toString()) }

        }

        // editTextPassword 텍스트 체인지 리스너
        binding.editTextPassword.addTextChangedListener { text ->

            text?.let { signUpViewModel.validationPassword(it.toString()) }

        }

        // editTextReConfirmPassword 텍스트 체인지 리스너
        binding.editTextReconfirmPassword.addTextChangedListener { text ->

            text?.let { signUpViewModel.correctPassword(binding.editTextPassword.text.toString(),it.toString()) }

        }

        // editTextNickname 텍스트 체인지 리스너
        binding.editTextNickname.addTextChangedListener { text ->

            text?.let {

                val filtered = it.toString().replace(" ", "")

                if (filtered != it.toString()) {

                    binding.editTextNickname.setText(filtered)
                    binding.editTextNickname.setSelection(filtered.length) // 커서를 끝으로 이동

                }

                signUpViewModel.validationNickname(it.toString())

            }

        }

        // 에딧텍스트 코드 포커스 체인지 리스너
        binding.editTextCode.setOnFocusChangeListener { _, hasFocus ->

            signUpViewModel.onCodeFocusChanged(hasFocus)

        }

        // 에딧텍스트 패스워드 포커스 체인지 리스너
        binding.editTextPassword.setOnFocusChangeListener{ _, hasFocus ->

            signUpViewModel.onPasswordFocusChanged(hasFocus)

        }

        // 에딧텍스트 패스워드 재확인 포커스 체인지 리스너
        binding.editTextReconfirmPassword.setOnFocusChangeListener{ _, hasFocus ->

            signUpViewModel.onReconfirmPasswordFocusChanged(hasFocus)

        }

    } // setListener()


    // Observe 관련 메서드
    private fun setObserve() {

        // 페이지 카운트 observe
        signUpViewModel.pageCount.observe(viewLifecycleOwner) { setPageCount(it)}

        // 에딧텍스트 이메일 관찰
        signUpViewModel.isEmailFocus.observe(viewLifecycleOwner) {

            when(it){

                true -> binding.editTextEmail.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.personal)

                false ->  binding.editTextEmail.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.grey)

            }

        }

        // 에딧텍스트 전송코드 관찰
        signUpViewModel.isCodeFocus.observe(viewLifecycleOwner) {

            when(it){

                true -> binding.editTextCode.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.personal)

                false -> binding.editTextCode.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.grey)

            }

        }

        // 에딧텍스트 패스워드 관찰
        signUpViewModel.isPasswordFocus.observe(viewLifecycleOwner) {

            when(it){

                true -> binding.editTextPassword.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.personal)

                false -> binding.editTextPassword.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.grey)

            }

        }

        // 에딧텍스트 패스워드재확인 관찰
        signUpViewModel.isReconfirmPasswordFocus.observe(viewLifecycleOwner) {

            when(it){

                true -> binding.editTextReconfirmPassword.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.personal)

                false -> binding.editTextReconfirmPassword.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.grey)

            }

        }

        // 이메일 유효성 관찰
        signUpViewModel.isEmailValid.observe(viewLifecycleOwner) { setEmailValid(it) }

        // 코드 유효성 관찰
        signUpViewModel.isCodeValid.observe(viewLifecycleOwner){

            when(it){

                "true" -> Toast.makeText(requireContext(), getString(R.string.code_verification_successful), Toast.LENGTH_SHORT).show()

                "wrong" -> Toast.makeText(requireContext(), getString(R.string.code_verification_failure),Toast.LENGTH_SHORT).show()

                else -> Toast.makeText(requireContext(), getString(R.string.code_verification_resend),Toast.LENGTH_SHORT).show()

            }

        }


        // 타이머 관찰
        signUpViewModel.timerCount.observe(viewLifecycleOwner) {

            if(it == 0) {

                Toast.makeText(requireContext(), getString(R.string.code_verification_resend), Toast.LENGTH_SHORT).show()

            }

        }

        // 사용 가능 이메일 관찰
        signUpViewModel.isEmailPossible.observe(viewLifecycleOwner){

            when(it){

                true -> {

                    binding.linearLayoutCode.visibility = View.VISIBLE
                    Toast.makeText(requireContext(),getString(R.string.canUseEmail), Toast.LENGTH_SHORT).show()

                }

                false ->Toast.makeText(requireContext(),getString(R.string.alreadyExistEmail), Toast.LENGTH_SHORT).show()

            }

        }

        // 이메일 전송 관찰
        signUpViewModel.isEmailSend.observe(viewLifecycleOwner){

            when(it){

                true -> {

                    Toast.makeText(requireContext(), getString(R.string.codeTransmission), Toast.LENGTH_SHORT).show()
                    binding.buttonNext.isEnabled = true

                }

                false ->Toast.makeText(requireContext(), getString(R.string.failCodeTransmission), Toast.LENGTH_SHORT).show()

            }

        }

        // 패스워드 유효성 관찰
        signUpViewModel.isPasswordValid.observe(viewLifecycleOwner){ setPasswordValid(it) }

        // 패스워드 일치 여부 관찰
        signUpViewModel.isPasswordCorrect.observe(viewLifecycleOwner){ setPasswordCorrect(it) }

        // 닉네임 유효성 여부 관찰
        signUpViewModel.isNicknameValid.observe(viewLifecycleOwner){setNicknameValid(it) }

        // 닉네임 중복 여부
        signUpViewModel.isNicknamePossible.observe(viewLifecycleOwner){

            when(it){

                true -> Toast.makeText(requireContext(), getString(R.string.canUseNickname), Toast.LENGTH_SHORT).show()

                false -> Toast.makeText(requireContext(), getString(R.string.alreadyExistNickname), Toast.LENGTH_SHORT).show()

            }

        }

        // 회원가입 성공 여부 관찰
        signUpViewModel.isSignUpSuccess.observe(viewLifecycleOwner){

            when(it){

                true -> {

                    Toast.makeText(requireContext(), getString(R.string.successSignUp), Toast.LENGTH_SHORT).show()
                    this.findNavController().popBackStack()

                }

                false -> Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()

            }

        }

    } // setObserve()


    // 페이지에 따른 View처리 정리
    private fun setPageCount(it:Int){

        binding.buttonNext.isEnabled = false
        binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)

        when(it) {

            0 -> {

                this.findNavController().popBackStack()

            }

            1 -> {

                binding.linearLayout1.visibility = View.VISIBLE
                binding.linearLayout2.visibility = View.GONE
                binding.linearLayout3.visibility = View.GONE
                binding.linearLayout4.visibility = View.GONE
                binding.buttonNext.visibility = View.VISIBLE
                binding.editTextEmail.setText("")
                binding.textViewEmailValid.text = ""
                binding.editTextCode.setText("")
                signUpViewModel.changeCourseValue("duplicateCheck")

            }

            2 -> {

                binding.linearLayout1.visibility = View.GONE
                binding.linearLayout2.visibility = View.VISIBLE
                binding.linearLayout3.visibility = View.GONE
                binding.linearLayout4.visibility = View.GONE
                binding.buttonNext.visibility = View.VISIBLE
                binding.editTextPassword.setText("")
                binding.textViewPasswordValid.text = ""
                binding.textViewPasswordCorrect.text = ""
                binding.editTextReconfirmPassword.setText("")
                signUpViewModel.changeCourseValue("passwordCheck")

            }

            3 -> {

                binding.linearLayout1.visibility = View.GONE
                binding.linearLayout2.visibility = View.GONE
                binding.linearLayout3.visibility = View.VISIBLE
                binding.linearLayout4.visibility = View.GONE
                binding.buttonNext.visibility = View.VISIBLE
                binding.buttonComplete.visibility = View.GONE
                binding.textViewNicknameValid.text = ""
                binding.editTextNickname.setText("")
                signUpViewModel.changeCourseValue("nicknameCheck")

            }

            4 -> {

                binding.linearLayout1.visibility = View.GONE
                binding.linearLayout2.visibility = View.GONE
                binding.linearLayout3.visibility = View.GONE
                binding.linearLayout4.visibility = View.VISIBLE
                binding.buttonNext.visibility = View.GONE
                binding.buttonComplete.visibility = View.VISIBLE
                signUpViewModel.changeCourseValue("lastCheck")

            }

        }

    } // setPageCount()


    //이메일 유효성 관찰에 대한 처리
    private fun setEmailValid(it:Boolean){

        when(it){

            true -> {

                binding.buttonNext.isEnabled = true
                binding.buttonNext.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.personal)

                binding.textViewEmailValid.visibility = View.VISIBLE

                binding.textViewEmailValid.setTextColor(ContextCompat.getColor(requireContext(), R.color.personal))

                binding.textViewEmailValid.text = getString(R.string.validEmailFormat)

            }

            false -> {

                binding.buttonNext.isEnabled = false
                binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)

                binding.linearLayoutCode.visibility = View.GONE

                binding.textViewEmailValid.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))

                binding.textViewEmailValid.text = getString(R.string.inValidEmailFormat)

            }

        }

    } //setEmailValid()


    // 패스워드 유효성 관찰에 대한 처리
    private fun setPasswordValid(it:Boolean){

        when(it){

            true -> {

                binding.editTextReconfirmPassword.visibility = View.VISIBLE
                binding.textViewPasswordValid.text = getString(R.string.validPasswordFormat)
                binding.textViewPasswordValid.setTextColor(ContextCompat.getColor(requireContext(),R.color.personal))

            }

            false -> {

                binding.editTextReconfirmPassword.visibility = View.GONE
                binding.textViewPasswordCorrect.visibility = View.GONE
                binding.textViewPasswordValid.visibility = View.VISIBLE
                binding.textViewPasswordValid.text = getString(R.string.inValidPasswordFormat)
                binding.textViewPasswordValid.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))

            }

        }

    } // setPasswordValid()


    //패스워드 일치 관찰에 대한 처리
    private fun setPasswordCorrect(it:Boolean){

        when(it){

            true -> {

                if(binding.editTextPassword.text.toString() != ""){

                    binding.buttonNext.isEnabled = true
                    binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
                    binding.textViewPasswordCorrect.visibility = View.VISIBLE
                    binding.textViewPasswordCorrect.text = getString(R.string.correctPasswordFormat)
                    binding.textViewPasswordCorrect.setTextColor(ContextCompat.getColor(requireContext(),R.color.personal))

                }

            }

            false -> {

                binding.buttonNext.isEnabled = false
                binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
                binding.textViewPasswordCorrect.visibility = View.VISIBLE
                binding.textViewPasswordCorrect.text = getString(R.string.inCorrectPasswordFormat)
                binding.textViewPasswordCorrect.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))

            }

        }

    } // setPasswordValid()


    // 닉네임 유효성 관찰에 대한 처리
    private fun setNicknameValid(it:Boolean){

        if(binding.editTextNickname.text.toString() != ""){

            when(it){

                true ->{

                    binding.buttonNext.isEnabled = true
                    binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.personal)
                    binding.textViewNicknameValid.visibility = View.VISIBLE
                    binding.textViewNicknameValid.text = getString(R.string.validNicknameFormat)
                    binding.textViewNicknameValid.setTextColor(ContextCompat.getColor(requireContext(),R.color.personal))

                }

                false -> {

                    binding.buttonNext.isEnabled = false
                    binding.buttonNext.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
                    binding.textViewNicknameValid.visibility = View.VISIBLE
                    binding.textViewNicknameValid.text = getString(R.string.inValidNicknameFormat)
                    binding.textViewNicknameValid.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))

                }

            }

        }

    } // setNicknameValid()


    //커스텀 다이얼로그 띄우기
    private fun setNetworkCustomDialog(){

        // 부모가 있는지 확인하고, 있다면 부모에서 제거
        customNetworkDialogBinding.root.parent?.let {
            (it as ViewGroup).removeView(customNetworkDialogBinding.root)
        }

        //다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(customNetworkDialogBinding.root)
            .setCancelable(true)
            .create()

        //뒷배경 투명으로 바꿔서 둥근모서리 보이게
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customNetworkDialogBinding.textViewButtonOk.setOnClickListener {

            dialog.dismiss()

        }

        dialog.setOnCancelListener {

            dialog.dismiss()

        }

        dialog.show()

    } // setNetworkCustomDialog()

}