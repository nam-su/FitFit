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
    var challengeItemClick: ChallengeItemClick? = null

    // 프래그먼트에서 아이템 클릭 리스너 호출하기 위한 인터페이스
    interface ChallengeItemClick{
        fun onClick(view: View, challenge: Challenge)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeListViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_challenge_list,parent,false)

        return ChallengeListViewHolder(binding)

    } // onCreateViewHolder()

    override fun getItemCount(): Int {

        return challengeList.size

    } // getItemCount()

    override fun onBindViewHolder(holder: ChallengeListViewHolder, position: Int) {

        holder.onBind(challengeList[position])

        // 아이템 클릭 리스너가 null이 아닐때 클릭리스너 연동
        if(challengeItemClick != null) {

            // 레이아웃 클릭 했을 때 클릭이벤트 발생
            holder.binding.linearLayoutChallenge.setOnClickListener {

                challengeItemClick!!.onClick(it,challengeList[position])

            }

        }


    } // onBindViewHolder()

    class ChallengeListViewHolder(val binding: ItemViewChallengeListBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(challenge: Challenge) {

            binding.challenge = challenge

        } // onBind()


    }

}