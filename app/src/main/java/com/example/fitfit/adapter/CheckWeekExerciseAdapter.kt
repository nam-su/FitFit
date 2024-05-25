package com.example.fitfit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.ExerciseDiary
import com.example.fitfit.databinding.ItemViewCheckWeekExerciseBinding

class CheckWeekExerciseAdapter: RecyclerView.Adapter<CheckWeekExerciseAdapter.CheckWeekExerciseViewHolder>() {

    lateinit var binding: ItemViewCheckWeekExerciseBinding

    private val checkWeekExerciseList = ArrayList<ExerciseDiary>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckWeekExerciseViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_view_check_week_exercise,parent,false)

        return CheckWeekExerciseViewHolder(binding)

    } // onCreateViewHolder()


    override fun getItemCount(): Int {

        return checkWeekExerciseList.size

    } // getItemCount()


    override fun onBindViewHolder(holder: CheckWeekExerciseViewHolder, position: Int) {

        holder.onBind(checkWeekExerciseList[position])

    } // onBindViewHolder()


    class CheckWeekExerciseViewHolder(val binding: ItemViewCheckWeekExerciseBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(exerciseDiary: ExerciseDiary) {



        } // onBind

    }

}