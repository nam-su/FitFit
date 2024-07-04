package com.example.fitfit.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitfit.R
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.ExerciseItemInfo
import com.example.fitfit.databinding.ItemViewChallengeBinding
import com.example.fitfit.databinding.ItemViewExerciseItemInfoImageBinding
import com.example.fitfit.viewModel.ExerciseViewModel

class ChallengeAdapter(private val challengeList: ArrayList<Challenge>, private val context: Context, private val exerciseViewModel: ExerciseViewModel): RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    private val TAG = "챌린지 어댑터"

    lateinit var binding: ItemViewChallengeBinding
    var challengeItemClick: ChallengeAdapter.ChallengeItemClick? = null

    // 프래그먼트에서 아이템 클릭 리스너 호출하기 위한 인터페이스
    interface ChallengeItemClick{
        fun onClick(view: View, challenge: Challenge)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_challenge,parent,false)

        return ChallengeViewHolder(binding)

    } // onCreateViewHolder()


    override fun getItemCount(): Int = challengeList.size


    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {

        holder.onBind(challengeList[position], context, exerciseViewModel)


        // 아이템 클릭 리스너가 null이 아닐때 클릭리스너 연동
        if(challengeItemClick != null) {

            // 레이아웃 클릭 했을 때 클릭이벤트 발생
           holder.binding.imageViewChallenge.setOnClickListener {
               challengeItemClick!!.onClick(it, challengeList[position])
           }

        }


    } // onBindViewHolder()


    class ChallengeViewHolder(val binding: ItemViewChallengeBinding) : RecyclerView.ViewHolder(binding.root){

        fun onBind(challenge: Challenge,context: Context,exerciseViewModel: ExerciseViewModel) {

            Glide.with(context)
                //baseurl+쉐어드의 이미지경로
                .load(exerciseViewModel.getBaseUrl()+challenge.challengeImage)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.loading)
                .into(binding.imageViewChallenge)

            binding.textViewParticipantCount.text = "${challenge.participantCount}명 참가중"

        } // onBind()

    }

}