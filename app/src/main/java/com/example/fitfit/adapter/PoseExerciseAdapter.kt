package com.example.fitfit.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.databinding.ItemViewPoseExerciseBinding

class PoseExerciseAdapter(private val poseExerciseList: ArrayList<PoseExercise>,private val checkVisibleExerciseName: Boolean): RecyclerView.Adapter<PoseExerciseAdapter.PoseExerciseViewHolder>() {

    lateinit var binding: ItemViewPoseExerciseBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoseExerciseViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_pose_exercise,parent,false)

        return PoseExerciseViewHolder(binding)

    } // onCreateViewHolder()

    override fun getItemCount(): Int {

        return poseExerciseList.size

    } // getItemCount()

    override fun onBindViewHolder(holder: PoseExerciseViewHolder, position: Int) {

        holder.onBind(poseExerciseList[position],checkVisibleExerciseName)

    } // onBindViewHolder()

    class PoseExerciseViewHolder(val binding: ItemViewPoseExerciseBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(poseExercise: PoseExercise,checkVisibleExerciseName: Boolean) {

            // 운동 객체가 갖고 있는 카테고리에 따라 뷰 지정
            setPoseExerciseView(poseExercise.category)

            // 운동이름 판별 후 textView 처리함.
            when(checkVisibleExerciseName) {

                // 운동 아이콘 + 운동 이름 보이게
                true -> binding.textViewExerciseName.text = poseExercise.exerciseName

                // 운동 이름이 안보이고 운동 아이콘 이미지만 보이게
                else -> binding.textViewExerciseName.visibility = View.GONE

            }

        } // onBind()


        // 운동 종류에 따라 뷰 초기화 하는 메서드
        private fun setPoseExerciseView(category: String){

            when(category) {

                "스쿼트" -> {

                    binding.imageViewExerciseCategory.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.squat))

                    binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_squat)

                }

                "푸시업" -> {

                    binding.imageViewExerciseCategory.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.pushUp))

                    binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_push_up)

                }

                "런지" -> {

                    binding.imageViewExerciseCategory.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.lunge))

                    binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_lunge)

                }

            }

        } // setPoseExerciseView()

    }

}