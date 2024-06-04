package com.example.fitfit.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.databinding.CustomDialogTwoButtonBinding
import com.example.fitfit.databinding.FragmentUserBinding
import com.example.fitfit.databinding.FragmentUserEditBinding
import com.example.fitfit.viewModel.UserEditViewModel
import com.example.fitfit.viewModel.UserViewModel

class UserEditFragment : Fragment() {

    private val TAG = "유저 프래그먼트"

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

    }



    //setObserve()
    private fun setObserve(){

        userEditViewModel.selectedImageUri.observe(viewLifecycleOwner) {
                Glide.with(this)
                    .load(it)
                    .into(binding.circleImageViewUserProfile)
            }
        }

    }


