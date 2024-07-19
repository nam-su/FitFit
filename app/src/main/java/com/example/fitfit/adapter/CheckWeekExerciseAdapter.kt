package com.example.fitfit.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.ExerciseDiary
import com.example.fitfit.databinding.ItemViewCheckWeekExerciseBinding

class CheckWeekExerciseAdapter(private val checkWeekExerciseList: ArrayList<ExerciseDiary>): RecyclerView.Adapter<CheckWeekExerciseAdapter.CheckWeekExerciseViewHolder>() {

    lateinit var binding: ItemViewCheckWeekExerciseBinding

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckWeekExerciseViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_view_check_week_exercise,parent,false)

        return CheckWeekExerciseViewHolder(binding)

    } // onCreateViewHolder()


    // getItemCount
    override fun getItemCount(): Int = checkWeekExerciseList.size
    // getItemCount()


    // onBindViewHolder
    override fun onBindViewHolder(holder: CheckWeekExerciseViewHolder, position: Int) =
        holder.onBind(checkWeekExerciseList[position])
    // onBindViewHolder()


    // 뷰홀더 클래스
    class CheckWeekExerciseViewHolder(private val binding: ItemViewCheckWeekExerciseBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(exerciseDiary: ExerciseDiary) {

            binding.textViewDay.text = exerciseDiary.day

            // 요일에 색상 추가
            binding.textViewDay.setTextColor(

                when (exerciseDiary.check) {

                    true -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.personal))
                    else -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.black))

                })

            // 네모칸에 색상 추가
            binding.viewCheckExercise.backgroundTintList = when(exerciseDiary.check){

                true -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.personal))
                else -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.grey))

            }

        } // onBind

    }

}