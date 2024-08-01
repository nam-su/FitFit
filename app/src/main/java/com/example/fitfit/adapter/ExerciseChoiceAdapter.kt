package com.example.fitfit.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.activity.MainActivity
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.data.Rank
import com.example.fitfit.databinding.ItemViewExerciseChoiceBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseChoiceAdapter(val exerciseChoiceList: ArrayList<PoseExercise>): RecyclerView.Adapter<ExerciseChoiceAdapter.ExerciseChoiceViewHolder>() {

    lateinit var binding: ItemViewExerciseChoiceBinding
    var exerciseChoiceItemClick: ExerciseChoiceItemClick? = null

    // 프래그먼트에서 아이템 클릭 리스너 호출하기 위한 인터페이스
    interface ExerciseChoiceItemClick{
        fun onClick(view: View,position: Int)
        // onClick

    }


    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseChoiceViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_exercise_choice,parent,false)

        return ExerciseChoiceViewHolder(binding)

    } // onCreateViewHolder()


    // getItemCount
    override fun getItemCount(): Int = exerciseChoiceList.size
    // getItemCount()


    // onBindViewHolder
    override fun onBindViewHolder(holder: ExerciseChoiceViewHolder, position: Int) {

        holder.onBind(exerciseChoiceList[position])

        // 아이템 클릭 리스너가 null이 아닐때 클릭리스너 연동
        if(exerciseChoiceItemClick != null) {

            // 레이아웃 클릭 했을 때 클릭이벤트 발생
            holder.binding.constraintLayoutStartExercise.setOnClickListener {

                exerciseChoiceItemClick!!.onClick(it,position)
                Log.d("TAG", "onBindViewHolder: ${exerciseChoiceList[position].date}")

            }

        }

    } // onBindViewHolder()


    // 뷰홀더 클래스
    class ExerciseChoiceViewHolder(val binding: ItemViewExerciseChoiceBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(poseExercise: PoseExercise) {

            binding.poseExercise = poseExercise

            // 운동 객체가 갖고있는 카테고리에 따라 배경색 지정.
            binding.constraintLayoutStartExercise.backgroundTintList = when(poseExercise.category) {

                "스쿼트" -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.squat))

                "푸시업" -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.pushUp))

                "런지" -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.lunge))

                "레그레이즈" -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.legRaises))

                else -> {ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.personal))}

            }

        } // onBind()

    }

}