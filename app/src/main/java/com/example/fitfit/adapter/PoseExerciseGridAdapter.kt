package com.example.fitfit.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.PoseExercise
import com.example.fitfit.databinding.ItemViewPoseExerciseGridBinding

class PoseExerciseGridAdapter(private val poseExerciseList: ArrayList<PoseExercise>): RecyclerView.Adapter<PoseExerciseGridAdapter.PoseExerciseGridViewHolder>() {

    lateinit var binding: ItemViewPoseExerciseGridBinding


    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoseExerciseGridViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_pose_exercise_grid,parent,false)

        return PoseExerciseGridViewHolder(binding)

    } // onCreateViewHolder()


    // getItemCount
    override fun getItemCount(): Int = poseExerciseList.size
    // getItemCount()


    // onBindViewHolder
    override fun onBindViewHolder(holder: PoseExerciseGridViewHolder, position: Int) = holder.onBind(poseExerciseList[position])
    // onBindViewHolder


    // 뷰홀더 클래스
    class PoseExerciseGridViewHolder(val binding: ItemViewPoseExerciseGridBinding): RecyclerView.ViewHolder(binding.root) {

        private val context = itemView.context

        fun onBind(poseExercise: PoseExercise) {

            // 구독 후 이용가능한 프리미엄 운동이면 뱃지 보이게
            when(poseExercise.isPrimium) {

                1 -> binding.imageViewPremiumBadge.visibility = View.VISIBLE
                0 -> binding.imageViewPremiumBadge.visibility = View.GONE

            }

            setPoseExerciseView(poseExercise)

        } // onBind()


        private fun setPoseExerciseView(poseExercise: PoseExercise){

            when (poseExercise.category) {

                context.getString(R.string.squat) -> {

                    binding.imageViewExerciseCategory.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.squat
                            )
                        )

                }

                context.getString(R.string.pushUp) -> {

                    binding.imageViewExerciseCategory.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.pushUp
                            )
                        )

                    binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_push_up)

                }

                context.getString(R.string.lunge) -> {

                    binding.imageViewExerciseCategory.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.lunge
                            )
                        )

                }

                context.getString(R.string.legRaises) -> {

                    binding.imageViewExerciseCategory.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.legRaises
                            )
                        )

                }

            }

            setExerciseImageResource(poseExercise.exerciseName)
            binding.imageViewExerciseCategory.setColorFilter(Color.WHITE)

        } // setPoseExerciseView()

        private fun setExerciseImageResource(exerciseName: String) {

            when(exerciseName) {

                context.getString(R.string.basicSquat) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_squat)

                context.getString(R.string.wideSquat) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.wide_squat)

                context.getString(R.string.basicPushUp) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_push_up)

                context.getString(R.string.basicLunge) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_lunge)

                context.getString(R.string.leftLunge) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.left_lunge)

                context.getString(R.string.rightLunge) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.right_lunge)

                context.getString(R.string.basicLegRaises) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_leg_raises)

                context.getString(R.string.leftLegRaises) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.left_leg_raises)

                context.getString(R.string.rightLegRaises) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.right_leg_raises)

            }

        } // setExerciseImageResource()

    }

}