package com.example.fitfit.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.ExerciseChoice
import com.example.fitfit.data.Rank
import com.example.fitfit.databinding.ItemViewExerciseChoiceBinding

class ExerciseChoiceAdapter(private val exerciseChoiceList: ArrayList<ExerciseChoice>): RecyclerView.Adapter<ExerciseChoiceAdapter.ExerciseChoiceViewHolder>() {

    lateinit var binding: ItemViewExerciseChoiceBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseChoiceViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_exercise_choice,parent,false)

        return ExerciseChoiceViewHolder(binding)

    } // onCreateViewHolder()


    override fun getItemCount(): Int {

        return exerciseChoiceList.size

    } // getItemCount()


    override fun onBindViewHolder(holder: ExerciseChoiceViewHolder, position: Int) {

        holder.onBind(exerciseChoiceList[position])

    } // onBindViewHolder()


    class ExerciseChoiceViewHolder(val binding: ItemViewExerciseChoiceBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(exerciseChoice: ExerciseChoice) {

            binding.constraintLayoutStartExercise.backgroundTintList = when(exerciseChoice.category) {

                "스쿼트" -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.squat))
                "푸시업" -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.pushUp))
                "런지" -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.lunge))
                else -> {ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.personal))}

            }

            binding.textViewExerciseCategory.text = exerciseChoice.category
            binding.textViewExerciseName.text = exerciseChoice.exerciseName

        } // onBind()

    }

}