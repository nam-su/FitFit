package com.example.fitfit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.ExerciseItemInfo
import com.example.fitfit.databinding.ItemViewExerciseItemInfoImageBinding

class ExerciseItemInfoAdapter(private val exerciseItemInfo: ExerciseItemInfo): RecyclerView.Adapter<ExerciseItemInfoAdapter.ExerciseItemInfoViewHolder>() {

    lateinit var binding: ItemViewExerciseItemInfoImageBinding


    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseItemInfoViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_exercise_item_info_image,parent,false)

        return ExerciseItemInfoViewHolder(binding)

    } // onCreateViewHolder()


    // getItemCount
    override fun getItemCount(): Int = 2
    // getItemCount()


    // onBindViewHolder
    override fun onBindViewHolder(holder: ExerciseItemInfoViewHolder, position: Int) {

        when(position) {

            0 -> holder.onBind(exerciseItemInfo.indexImage0)
            1 -> holder.onBind(exerciseItemInfo.indexImage1)

        }

    } // onBindViewHolder()


    // 뷰홀더 클래스
    class ExerciseItemInfoViewHolder(val binding: ItemViewExerciseItemInfoImageBinding) : RecyclerView.ViewHolder(binding.root){

        fun onBind(imageUrl: Int) {

            binding.imageViewExerciseItemInfo.setImageResource(imageUrl)

        } // onBind()

    }

}