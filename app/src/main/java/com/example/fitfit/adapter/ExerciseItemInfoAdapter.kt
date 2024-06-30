package com.example.fitfit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.ExerciseItemInfo
import com.example.fitfit.databinding.ItemViewExerciseItemInfoImageBinding

class ExerciseItemInfoAdapter(private val exerciseItemInfo: ExerciseItemInfo): RecyclerView.Adapter<ExerciseItemInfoAdapter.ExerciseItemInfoViewHolder>() {

    lateinit var binding: ItemViewExerciseItemInfoImageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseItemInfoViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_exercise_item_info_image,parent,false)

        return ExerciseItemInfoViewHolder(binding)

    } // onCreateViewHolder()


    override fun getItemCount(): Int = 2


    override fun onBindViewHolder(holder: ExerciseItemInfoViewHolder, position: Int) {

        when(position) {

            0 -> holder.onBind(exerciseItemInfo.indexImage0)
            1 -> holder.onBind(exerciseItemInfo.indexImage1)

        }

    } // onBindViewHolder()


    class ExerciseItemInfoViewHolder(val binding: ItemViewExerciseItemInfoImageBinding) : RecyclerView.ViewHolder(binding.root){

        fun onBind(imageUrl: Int) {

            binding.imageViewExerciseItemInfo.setImageResource(imageUrl)

        } // onBind()

    }

}