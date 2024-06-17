package com.android.blendit.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.databinding.ProductItemBinding
import com.android.blendit.remote.response.ItemsProduct
import com.bumptech.glide.Glide

class AdapterListProduct : PagingDataAdapter<ItemsProduct, AdapterListProduct.StoryViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsProduct>() {
            override fun areItemsTheSame(oldItem: ItemsProduct, newItem: ItemsProduct): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsProduct, newItem: ItemsProduct): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    inner class StoryViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ItemsProduct) {
            with(binding) {
                Glide.with(itemView).load(product.picture).into(imageView)
                brand.text = product.brand
                productName.text = product.productName

//                itemView.setOnClickListener {
//                    val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
//                        putExtra(DetailStoryActivity.USERNAME, story.name)
//                        putExtra(DetailStoryActivity.DESCRIPTION, story.description)
//                        putExtra(DetailStoryActivity.PHOTO, story.photoUrl)
//                        putExtra(DetailStoryActivity.LAT, story.lat)
//                        putExtra(DetailStoryActivity.LON, story.lon)
//                    }
//                    it.context.startActivity(intent)
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}