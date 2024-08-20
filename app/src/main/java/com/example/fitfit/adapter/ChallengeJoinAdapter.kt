package com.example.fitfit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfit.R
import com.example.fitfit.data.Challenge
import com.example.fitfit.databinding.ItemViewChallengeJoinBinding


class ChallengeJoinAdapter(private val myChallengeList: ArrayList<Challenge>): RecyclerView.Adapter<ChallengeJoinAdapter.ChallengeViewHolder>() {

    lateinit var binding: ItemViewChallengeJoinBinding

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_challenge_join,parent,false)

        return ChallengeViewHolder(binding)

    } // onCreateViewHolder()


    // getItemCount
    override fun getItemCount(): Int = myChallengeList.size
    // getItemCount()


    // onBindViewHolder
    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) = holder.onBind(myChallengeList[position])
    // onBindViewHolder()


    // 뷰홀더 클래스
    class ChallengeViewHolder(val binding: ItemViewChallengeJoinBinding) : RecyclerView.ViewHolder(binding.root){

        fun onBind(challenge: Challenge) {

            binding.textViewChallengeName.text = challenge.challengeName

            val context = itemView.context

            when(challenge.challengeName){

                "기본부터 챌린지" -> {

                    binding.progressBar1.apply {

                        visibility = View.VISIBLE
                        max = challenge.standard.toInt()
                        progress = challenge.rankingPoint

                    }

                    binding.textViewCount.text = context.getString(R.string.challengeCountText, challenge.rankingPoint.toString() , challenge.standard)

                }

                "한놈만 패! 챌린지" -> {

                    binding.textViewCount.text = context.getString(R.string.challengeCountFormat,challenge.standard,challenge.rankingPoint.toString())

                }

            }

            if(challenge.rankingPoint != 0) {

                binding.textViewRanking.text = context.getString(R.string.challengeRank,challenge.ranking)

            }

        } // onBind()

    }

}