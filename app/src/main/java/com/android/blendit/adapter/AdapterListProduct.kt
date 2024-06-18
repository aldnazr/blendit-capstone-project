package com.android.blendit.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.databinding.ProductItemBinding
import com.android.blendit.remote.response.ItemsProduct
import com.android.blendit.ui.detailitem.DetailActivity
import com.bumptech.glide.Glide

class AdapterListProduct :
    PagingDataAdapter<ItemsProduct, AdapterListProduct.StoryViewHolder>(DIFF_CALLBACK) {

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

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.BRAND, product.brand)
                        putExtra(DetailActivity.PRODUCT_NAME, product.productName)
                        putExtra(DetailActivity.PICTURE, product.picture)
                    }
                    it.context.startActivity(intent)
                }
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