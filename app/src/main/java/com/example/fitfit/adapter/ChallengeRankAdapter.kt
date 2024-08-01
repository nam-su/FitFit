package com.example.fitfit.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitfit.R
import com.example.fitfit.data.Rank
import com.example.fitfit.databinding.ItemViewChallengeRankBinding
import com.example.fitfit.viewModel.HomeViewModel

class ChallengeRankAdapter(private var challengeRankList: ArrayList<Rank>, private val homeViewModel: HomeViewModel): RecyclerView.Adapter<ChallengeRankAdapter.ChallengeRankViewHolder>() {

    lateinit var binding: ItemViewChallengeRankBinding
    var challengeRankItemClick: ChallengeRankItemClick? = null


    // 프래그먼트에서 아이템 클릭 리스너 호출하기 위한 인터페이스
    interface ChallengeRankItemClick{
        fun onClick(view: View, rank: Rank)
        // onClick()

    }


    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeRankViewHolder {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_challenge_rank,parent,false)

        return ChallengeRankViewHolder(binding, homeViewModel, parent.context)

    } // onCreateViewHolder()


    // getItemCount
    override fun getItemCount(): Int = challengeRankList.size
    // getItemCount()

    //setItems
    fun setItems(challengeRanks: ArrayList<Rank>?) {
        challengeRankList = challengeRankList
        notifyDataSetChanged()
    }


    // onBindViewHolder
    override fun onBindViewHolder(holder: ChallengeRankViewHolder, position: Int) {

        holder.onBind(challengeRankList!![position])

        Log.d("TAG", "onBindViewHolder: $challengeRankItemClick")
        // 아이템 클릭 리스너가 null이 아닐때 클릭리스너 연동
        if(challengeRankItemClick != null) {

            Log.d("TAG", "onBindViewHolder: challengeRankItemClick not null")

            // 레이아웃 클릭 했을 때 클릭이벤트 발생
            holder.binding.linearLayoutItem.setOnClickListener {

                Log.d("TAG", "onBindViewHolder: linearLayoutItem.setOnClickListener")

                challengeRankItemClick!!.onClick(it,challengeRankList[position])

            }

        }

    } // onBindViewHolder()


    // 뷰홀더 클래스
    class ChallengeRankViewHolder(
        val binding: ItemViewChallengeRankBinding,
        private val homeViewModel: HomeViewModel,
        private val context: Context?): RecyclerView.ViewHolder(binding.root) {

        fun onBind(rank: Rank) {

            binding.rank = rank

            when(rank.ranking){
                1 -> {
                    binding.textViewRank.visibility = View.GONE
                    binding.imageViewRank.visibility = View.VISIBLE
                    Glide.with(context!!).load(R.drawable.medal_gold).into(binding.imageViewRank)
                }
                2 -> {
                    binding.textViewRank.visibility = View.GONE
                    binding.imageViewRank.visibility = View.VISIBLE
                    Glide.with(context!!).load(R.drawable.medal_silver).into(binding.imageViewRank)

                }
                3 -> {
                    binding.textViewRank.visibility = View.GONE
                    binding.imageViewRank.visibility = View.VISIBLE
                    Glide.with(context!!).load(R.drawable.medal_bronze).into(binding.imageViewRank)

                }
            }

                when(rank.challengeName){

                    "기본부터 챌린지" -> binding.textViewPoint.text = "${rank.rankingPoint} / ${rank.standard} 일"

                    "한놈만 패! 챌린지" -> binding.textViewPoint.text = "${rank.standard} ${rank.rankingPoint} 회"

                }

            Glide.with(context!!)
                //baseurl+쉐어드의 이미지경로
                .load(homeViewModel.getBaseUrl()+rank.profileImagePath)
                .circleCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .thumbnail(Glide.with(context).load(R.raw.loading))
                .into(binding.imageViewUserProfile)

            if(homeViewModel.getUserId() == rank.id){

                binding.linearLayoutItem.setBackgroundResource(R.drawable.background_rectangular_personal_grey)

            }

        } // onBind()

    }

}