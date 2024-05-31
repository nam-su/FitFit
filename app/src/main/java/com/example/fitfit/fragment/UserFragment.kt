package com.example.fitfit.fragment

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.example.fitfit.R
import com.example.fitfit.databinding.FragmentUserBinding
import com.example.fitfit.viewModel.HomeViewModel
import com.example.fitfit.viewModel.UserViewModel
import kotlinx.coroutines.coroutineScope

class UserFragment : Fragment() {

    lateinit var binding: FragmentUserBinding
    lateinit var userViewModel: UserViewModel


    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        setVariable()


    } // // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        userViewModel = UserViewModel()
        binding.userViewModel = userViewModel

        userViewModel.setUserInformation()



    } // setVariable()



    //setListener(){
    fun setListener(){

        binding.imageButtonSetting.setOnClickListener {
           binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        binding.navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            binding.drawerLayout.close()
            true
        }
    }
}