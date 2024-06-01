package com.example.fitfit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.Rank
import com.example.fitfit.databinding.ItemViewChallengeRankBinding

class ChallengeRankAdapter(private val challengeRankList: ArrayList<Rank>): RecyclerView.Adapter<ChallengeRankAdapter.ChallengeRankViewHolder>() {

    lateinit var binding: ItemViewChallengeRankBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeRankViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_challenge_rank,parent,false)

        return ChallengeRankViewHolder(binding)

    } // onCreateViewHolder()

    override fun getItemCount(): Int {

        return challengeRankList.size

    } // getItemCount()

    override fun onBindViewHolder(holder: ChallengeRankViewHolder, position: Int) {

        holder.onBind(challengeRankList[position])

    } // onBindViewHolder()

    class ChallengeRankViewHolder(private val binding: ItemViewChallengeRankBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(rank: Rank) {

            binding.rank = rank

        } // onBind()

    }

}