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

class PoseExerciseAdapter(
    var poseExerciseList: ArrayList<PoseExercise>,
    private val checkVisibleExerciseName: Boolean,
    private val isCheckMyExerciseList: String
) : RecyclerView.Adapter<PoseExerciseAdapter.PoseExerciseViewHolder>() {

    lateinit var binding: ItemViewPoseExerciseBinding

    var exerciseEditItemAddButtonClick: ExerciseEditItemAddButtonClick? = null
    var exerciseEditItemDeleteButtonClick: ExerciseEditItemDeleteButtonClick? = null
    var exerciseItemClick: ExerciseItemClick? = null
    interface ExerciseEditItemAddButtonClick {

        fun onAddButtonClick(view: View,position: Int)
        // onAddButtonClick()

    }

    interface ExerciseEditItemDeleteButtonClick {

        fun onDeleteButtonClick(view: View,position: Int)
        // onDeleteButtonClick()

    }

    interface ExerciseItemClick {

        fun onExerciseItemClick(view: View,position: Int)
        // onExerciseItemClick()

    }


    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoseExerciseViewHolder {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_view_pose_exercise,
            parent,
            false
        )

        return PoseExerciseViewHolder(binding)

    } // onCreateViewHolder()


    // getItemCount
    override fun getItemCount(): Int = poseExerciseList.size
    // getItemCount()


    // onBindViewHolder
    override fun onBindViewHolder(holder: PoseExerciseViewHolder, position: Int) {

        holder.onBind(poseExerciseList[position], checkVisibleExerciseName)
        holder.setEditButton(isCheckMyExerciseList)

        // 아이템 클릭 리스너가 널이 아닐때 클릭 리스너 연동한다.

        // 운동 리스트 편집에서 아이템 추가 하는 리스너
        if (exerciseEditItemAddButtonClick != null) {

            holder.binding.imageButtonAddExerciseItem.setOnClickListener {

               exerciseEditItemAddButtonClick!!.onAddButtonClick(it,position)

            }

        }

        // 운동 리스트 편집에서 아이템 삭제 하는 리스너
        if(exerciseEditItemDeleteButtonClick != null) {

            holder.binding.imageButtonDeleteExerciseItem.setOnClickListener {

                exerciseEditItemDeleteButtonClick!!.onDeleteButtonClick(it,position)

            }

        }

        // 운동 아이템 클릭 이벤트 -> 운동 정보 보는 리스너
        if(exerciseItemClick != null) {

            holder.binding.imageViewExerciseCategory.setOnClickListener {

                exerciseItemClick!!.onExerciseItemClick(it,position)

            }

        }

    } // onBindViewHolder()


    // 뷰홀더 클래스
    class PoseExerciseViewHolder(val binding: ItemViewPoseExerciseBinding) : RecyclerView.ViewHolder(binding.root) {

        private val context = itemView.context

        fun onBind(poseExercise: PoseExercise, checkVisibleExerciseName: Boolean) {

            binding.poseExercise = poseExercise

            // 구독 후 이용가능한 프리미엄 운동이면 뱃지 보이게
            when (poseExercise.isPrimium) {

                1 -> binding.imageViewPremiumBadge.visibility = View.VISIBLE
                0 -> binding.imageViewPremiumBadge.visibility = View.GONE

            }

            // 운동 객체가 갖고 있는 카테고리에 따라 뷰 지정
            setPoseExerciseView(poseExercise)

            // 운동이름 판별 후 textView 처리함.
            when (checkVisibleExerciseName) {

                // 운동 아이콘 + 운동 이름 보이게
                true -> {

                    val splitExerciseName = poseExercise.exerciseName.split(" ")
                    binding.textViewExerciseName.text = splitExerciseName[0]
                    binding.textViewExerciseName.visibility = View.VISIBLE

                }

                // 운동 아이콘만 보이게
                else -> binding.textViewExerciseName.visibility = View.GONE

            }

        } // onBind()


        // 운동 종류에 따라 뷰 초기화 하는 메서드
        private fun setPoseExerciseView(poseExercise: PoseExercise) {

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

                context.getString(R.string.wideSquat)-> binding.imageViewExerciseCategory.setImageResource(R.drawable.wide_squat)

                context.getString(R.string.basicPushUp) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_push_up)

                context.getString(R.string.basicLunge) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_lunge)

                context.getString(R.string.leftLunge) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.left_lunge)

                context.getString(R.string.rightLunge) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.right_lunge)

                context.getString(R.string.basicLegRaises) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.basic_leg_raises)

                context.getString(R.string.leftLegRaises) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.left_leg_raises)

                context.getString(R.string.rightLunge) -> binding.imageViewExerciseCategory.setImageResource(R.drawable.right_leg_raises)

            }

        } // setExerciseImageResource()


        // 편집 프래그먼트에 진입했을때 버튼 초기화 하는 메서드
        fun setEditButton(isCheckMyExerciseList: String) {

            when (isCheckMyExerciseList) {

                "true" ->   binding.imageButtonDeleteExerciseItem.visibility = View.VISIBLE
                "false" ->  binding.imageButtonAddExerciseItem.visibility = View.VISIBLE

            }

        } // setEditButton()

    }

}