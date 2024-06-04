package com.example.fitfit.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.FragmentUserEditBinding
import com.example.fitfit.viewModel.UserEditViewModel

class UserEditFragment : Fragment() {

    private val TAG = "유저 에딧 프래그먼트"

    lateinit var binding: FragmentUserEditBinding
    lateinit var userEditViewModel: UserEditViewModel

    private val activityResultImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                userEditViewModel.setImageUri(uri,requireActivity())
            }
        }
    }


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_edit,container,false)

        setVariable()

        return binding.root

    } // onCreateView()



    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        setObserve()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        userEditViewModel = UserEditViewModel()
        binding.userEditViewModel = userEditViewModel

        userEditViewModel.setUserInformation()


    } // setVariable()



    //setListener(){
    private fun setListener(){

        binding.imageViewBack.setOnClickListener{
            this.findNavController().popBackStack()
            (activity as MainActivity).visibleBottomNavi()
        }

        binding.imageViewAdd.setOnClickListener {
            activityResultImage.launch(userEditViewModel.getGalleryIntent())
        }
        
        //nickname 다르게 입력할때마다 유효성 체크
        binding.editTextNickname.addTextChangedListener {
            userEditViewModel.validationNickname(it.toString())
        }


    }



    //setObserve()
    private fun setObserve(){

        userEditViewModel.selectedImageUri.observe(viewLifecycleOwner) {

                Glide.with(this)
                    .load(it)
                    .into(binding.circleImageViewUserProfile)

            setTextViewComplete(userEditViewModel.isNicknameValid.value)

        }


        userEditViewModel.isNicknameValid.observe(viewLifecycleOwner){

            if(!userEditViewModel.selectedImageUri.isInitialized) {
                if (it && userEditViewModel.nickname.value != binding.editTextNickname.text.toString()) {
                    setTextViewComplete(true)
                } else {
                    setTextViewComplete(false)
                }
            }else{
                setTextViewComplete(it)
            }

        }

        }


    //완료할수있는지 여부에 따른 뷰
    private fun setTextViewComplete(it: Boolean?){

        when(it){
            true -> {
                binding.textViewComplete.isEnabled = true
                binding.textViewComplete.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            }
            else -> {
                binding.textViewComplete.isEnabled = false
                binding.textViewComplete.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.halfGrey
                    )
                )

            }

        }
    }
    }


