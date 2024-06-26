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
import com.example.fitfit.databinding.ItemViewPoseExerciseBinding
import com.example.fitfit.databinding.ItemViewPoseExerciseGridBinding

class PoseExerciseGridAdapter(private val poseExerciseList: ArrayList<PoseExercise>): RecyclerView.Adapter<PoseExerciseGridAdapter.PoseExerciseGridViewHolder>() {

    lateinit var binding: ItemViewPoseExerciseGridBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoseExerciseGridViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_pose_exercise_grid,parent,false)

        return PoseExerciseGridViewHolder(binding)

    } // onCreateViewHolder()

    override fun getItemCount(): Int {

        return poseExerciseList.size

    } // getItemCount()

    override fun onBindViewHolder(holder: PoseExerciseGridViewHolder, position: Int) {

        holder.onBind(poseExerciseList[position])

    } // onBindViewHolder

    class PoseExerciseGridViewHolder(val binding: ItemViewPoseExerciseGridBinding): RecyclerView.ViewHolder(binding.root) {

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

                "스쿼트" -> {

                    binding.imageViewExerciseCategory.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.squat
                            )
                        )

                }

                "푸시업" -> {

                    binding.imageViewExerciseCategory.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.pushUp
                            )
                        )

                    binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_push_up)

                }

                "런지" -> {

                    binding.imageViewExerciseCategory.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.lunge
                            )
                        )

                }

                "레그레이즈" -> {

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

                "기본 스쿼트" -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_squat)
                "와이드 스쿼트" -> binding.imageViewExerciseCategory.setImageResource(R.drawable.wide_squat)
                "기본 푸시업" -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_push_up)
                "기본 런지" -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_lunge)
                "왼쪽 런지" -> binding.imageViewExerciseCategory.setImageResource(R.drawable.left_lunge)
                "오른쪽 런지" -> binding.imageViewExerciseCategory.setImageResource(R.drawable.right_lunge)
                "기본 레그레이즈" -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_leg_raises)
                "왼쪽 레그레이즈" -> binding.imageViewExerciseCategory.setImageResource(R.drawable.left_leg_raises)
                "오른쪽 레그레이즈" -> binding.imageViewExerciseCategory.setImageResource(R.drawable.right_leg_raises)

            }

        } // setExerciseImageResource()

    }

}