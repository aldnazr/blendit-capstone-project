package com.android.blendit.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.databinding.ItemProductBinding
import com.android.blendit.remote.response.ItemsFavorite
import com.android.blendit.remote.response.RecommendationResult
import com.android.blendit.ui.activity.detail.DetailActivity
import com.bumptech.glide.Glide

class AdapterRecommendation :
    ListAdapter<RecommendationResult, AdapterRecommendation.ViewHolder>(DIFF_CALLBACK) {

    private val listFavorite = mutableListOf<ItemsFavorite>()

    @SuppressLint("NotifyDataSetChanged")
    fun setFavoriteList(list: List<ItemsFavorite>) {
        listFavorite.clear()
        listFavorite.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recommendation = getItem(position)
        holder.bind(recommendation)
    }

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendation: RecommendationResult) {
            binding.brand.text = recommendation.brand
            binding.productName.text = recommendation.productName
            Glide.with(itemView.context)
                .load(recommendation.picture)
                .into(binding.imageView)

            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.ID, recommendation.id)
                    putExtra(DetailActivity.BRAND, recommendation.brand)
                    putExtra(DetailActivity.PRODUCT_NAME, recommendation.productName)
                    putExtra(DetailActivity.PICTURE, recommendation.picture)
                    putExtra(DetailActivity.TYPE, recommendation.type)
                    putExtra(DetailActivity.SKINTONE, recommendation.skintone)
                    putExtra(DetailActivity.SKINTYPE, recommendation.skinType)
                    putExtra(DetailActivity.UNDERTONE, recommendation.undertone)
                    putExtra(DetailActivity.SHADE, recommendation.shade)
                    putExtra(DetailActivity.MAKEUPTYPE, recommendation.makeupType)
                    for (favorite in listFavorite) {
                        if (favorite.productId == recommendation.id) {
                            putExtra(DetailActivity.IS_FAVORITE, true)
                        }
                    }
                }
                it.context.startActivity(intent)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationResult>() {
            override fun areItemsTheSame(
                oldItem: RecommendationResult,
                newItem: RecommendationResult
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RecommendationResult,
                newItem: RecommendationResult
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
