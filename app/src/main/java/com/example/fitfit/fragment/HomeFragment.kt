package com.example.fitfit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.fitfit.R
import com.example.fitfit.adapter.CheckWeekExerciseAdapter
import com.example.fitfit.databinding.FragmentHomeBinding
import com.example.fitfit.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var homeViewModel: HomeViewModel

    // onCreateView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)

        return binding.root

    } // onCreateView()


    // onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        homeViewModel = HomeViewModel()
        binding.homeViewModel = homeViewModel

        binding.recyclerViewCheckWeekExercise.adapter = CheckWeekExerciseAdapter()

        // 시작할때 통신을해서 viewModel에 어레이리스트 생성 후 observe해서 어뎁터 리스트에 꽂아준다?

    } // setVariable()

}