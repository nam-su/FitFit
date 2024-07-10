package com.example.fitfit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.Challenge
import com.example.fitfit.data.Rank
import com.example.fitfit.databinding.ItemViewChallengeListBinding
import com.example.fitfit.databinding.ItemViewChallengeRankBinding

class ChallengeListAdapter(private val challengeList: ArrayList<Challenge>): RecyclerView.Adapter<ChallengeListAdapter.ChallengeListViewHolder>() {

    lateinit var binding: ItemViewChallengeListBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeListViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_challenge_list,parent,false)

        return ChallengeListViewHolder(binding)

    } // onCreateViewHolder()

    override fun getItemCount(): Int {

        return challengeList.size

    } // getItemCount()

    override fun onBindViewHolder(holder: ChallengeListViewHolder, position: Int) {

        holder.onBind(challengeList[position])


    } // onBindViewHolder()

    class ChallengeListViewHolder(private val binding: ItemViewChallengeListBinding): RecyclerView.ViewHolder(binding.root) {

        private lateinit var challenge: Challenge

        fun onBind(challenge: Challenge) {

            binding.challenge = challenge

        } // onBind()


    }

}