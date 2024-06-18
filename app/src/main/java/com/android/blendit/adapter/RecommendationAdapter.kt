package com.android.blendit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.databinding.RecommendationItemBinding
import com.android.blendit.remote.response.RecommendationResult
import com.bumptech.glide.Glide
class RecommendationAdapter(
    private val onFavoriteClick: (RecommendationResult) -> Unit
) : ListAdapter<RecommendationResult, RecommendationAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecommendationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recommendation = getItem(position)
        holder.bind(recommendation)
    }

    inner class ViewHolder(private val binding: RecommendationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendation: RecommendationResult) {
            binding.brand.text = recommendation.brand
            binding.productName.text = recommendation.productName
            binding.shade.text = recommendation.shade
            Glide.with(itemView.context)
                .load(recommendation.picture)
                .into(binding.imageView)

            binding.favoriteButton.setOnClickListener {
                onFavoriteClick(recommendation)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationResult>() {
            override fun areItemsTheSame(oldItem: RecommendationResult, newItem: RecommendationResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecommendationResult, newItem: RecommendationResult): Boolean {
                return oldItem == newItem
            }
        }
    }
}
