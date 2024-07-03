package com.example.fitfit.adapter

import android.content.Context
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

    lateinit var binding: ItemViewChallengeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_challenge,parent,false)

        return ChallengeViewHolder(binding)

    } // onCreateViewHolder()


    override fun getItemCount(): Int = challengeList.size


    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {

        holder.onBind(challengeList[position], context, exerciseViewModel)

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