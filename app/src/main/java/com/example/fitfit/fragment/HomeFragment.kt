package com.example.fitfit.fragment

import android.app.Application
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfit.R
import com.example.fitfit.adapter.ChallengeRankAdapter
import com.example.fitfit.adapter.CheckWeekExerciseAdapter
import com.example.fitfit.adapter.PoseExerciseAdapter
import com.example.fitfit.adapter.PoseExerciseGridAdapter
import com.example.fitfit.databinding.FragmentHomeBinding
import com.example.fitfit.function.GridSpacingItemDecoration
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
        setClickListener()

    } // onViewCreated()


    // 변수 초기화
    private fun setVariable() {

        homeViewModel = HomeViewModel()
        binding.homeViewModel = homeViewModel

        binding.recyclerViewCheckWeekExercise.layoutManager = GridLayoutManager(activity?.applicationContext,7)
        binding.recyclerViewCheckWeekExercise.adapter = CheckWeekExerciseAdapter(homeViewModel.setRecyclerViewWeekStatus())

        binding.recyclerViewChallengeRank.adapter = ChallengeRankAdapter(homeViewModel.setRecyclerViewChallengeRank())

        binding.recyclerViewPagedAllExercise.adapter = PoseExerciseAdapter(homeViewModel.setRecyclerViewAllExercise(),false)
        binding.recyclerViewPagedAllExercise.layoutManager = LinearLayoutManager(activity?.applicationContext,LinearLayoutManager.HORIZONTAL,false)

        binding.recyclerViewAllExercise.layoutManager = GridLayoutManager(activity?.applicationContext,4)
        binding.recyclerViewAllExercise.addItemDecoration(GridSpacingItemDecoration(4,(10f * Resources.getSystem().displayMetrics.density).toInt()))

        binding.recyclerViewAllExercise.adapter = PoseExerciseGridAdapter(homeViewModel.setRecyclerViewAllExercise())

        // 시작할때 통신을해서 viewModel에 어레이리스트 생성 후 observe해서 어뎁터 리스트에 꽂아준다?

    } // setVariable()


    // 클릭 리스너 초기화
    private fun setClickListener() {

        binding.textViewViewAllExercise.setOnClickListener{

            binding.constraintLayoutHome.visibility = View.GONE
            binding.constraintLayoutAllExercise.visibility = View.VISIBLE

        }

        binding.imageButtonBackToHome.setOnClickListener{

            binding.constraintLayoutHome.visibility = View.VISIBLE
            binding.constraintLayoutAllExercise.visibility = View.GONE

        }

    } // setOnClickListener()

}