package com.example.fitfit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.ExerciseInfo
import com.example.fitfit.databinding.ItemViewDetailExerciseBinding

class ExerciseDetailViewAdapter(private val exerciseInfoList: ArrayList<ExerciseInfo>): RecyclerView.Adapter<ExerciseDetailViewAdapter.ExerciseDetailViewViewHolder>() {

    lateinit var binding: ItemViewDetailExerciseBinding


    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseDetailViewViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_detail_exercise,parent,false)

        return ExerciseDetailViewViewHolder(binding)

    } // onCreateViewHolder()


    // getItemCount
    override fun getItemCount(): Int = exerciseInfoList.size
    // getItemCount()


    // onBindViewHolder
    override fun onBindViewHolder(holder: ExerciseDetailViewViewHolder, position: Int) =
        holder.onBind(exerciseInfoList[position])
    // onBindViewHolder()


    // 뷰홀더 클래스
    class ExerciseDetailViewViewHolder(private val binding: ItemViewDetailExerciseBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(exerciseInfo: ExerciseInfo) {

            binding.exerciseInfo = exerciseInfo

        } // onBind()

    }

}