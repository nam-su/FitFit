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
import com.example.fitfit.databinding.ItemViewChallengeJoinBinding
import com.example.fitfit.databinding.ItemViewExerciseItemInfoImageBinding
import com.example.fitfit.viewModel.ExerciseViewModel

class ChallengeJoinAdapter(private val myChallengeList: ArrayList<Challenge>): RecyclerView.Adapter<ChallengeJoinAdapter.ChallengeViewHolder>() {

    private val TAG = "챌린지 참여리스트 어댑터"

    lateinit var binding: ItemViewChallengeJoinBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_challenge_join,parent,false)

        return ChallengeViewHolder(binding)

    } // onCreateViewHolder()


    override fun getItemCount(): Int = myChallengeList.size


    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {

        holder.onBind(myChallengeList[position])



    } // onBindViewHolder()


    class ChallengeViewHolder(val binding: ItemViewChallengeJoinBinding) : RecyclerView.ViewHolder(binding.root){

        fun onBind(challenge: Challenge) {

            binding.textViewChallengeName.text = challenge.challengeName

        } // onBind()

    }

}