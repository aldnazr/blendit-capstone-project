package com.android.androidexpert.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.androidexpert.core.R
import com.android.androidexpert.core.databinding.ItemProductBinding
import com.android.androidexpert.domain.model.ProductInfo
import com.facebook.shimmer.ShimmerFrameLayout

class AdapterListProduct : PagingDataAdapter<ProductInfo, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        const val ID = "id"
        const val PICTURE = "picture"
        const val BRAND = "brand"
        const val PRODUCT_NAME = "product_name"
        const val TYPE = "type"
        const val SKINTONE = "skintone"
        const val SKINTYPE = "skin_type"
        const val UNDERTONE = "undertone"
        const val SHADE = "shade"
        const val MAKEUPTYPE = "makeup_type"

        private const val ITEM_VIEW_TYPE_SHIMMER = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductInfo>() {
            override fun areItemsTheSame(oldItem: ProductInfo, newItem: ProductInfo): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProductInfo, newItem: ProductInfo): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemCount == 0
        return if (item) ITEM_VIEW_TYPE_SHIMMER else ITEM_VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = getItem(position)
            item?.let {
                holder.bind(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE_SHIMMER) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.shimmer_layout, parent, false)
            ShimmerViewHolder(view)
        } else {
            val binding =
                ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        }
    }

    inner class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val shimmerFrameLayout: ShimmerFrameLayout = itemView.findViewById(R.id.shimmer)

        init {
            shimmerFrameLayout.startShimmer()
        }
    }

    inner class ItemViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductInfo) {

            val detailActivity =
                Class.forName("com.android.androidexpert.presentation.detail.DetailActivity")

            with(binding) {
                com.bumptech.glide.Glide.with(itemView).load(product.picture).into(imageView)
                brand.text = product.brand
                productName.text = product.productName

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, detailActivity).apply {
                        putExtra(ID, product.id)
                        putExtra(BRAND, product.brand)
                        putExtra(PRODUCT_NAME, product.productName)
                        putExtra(PICTURE, product.picture)
                        putExtra(TYPE, product.type)
                        putExtra(SKINTONE, product.skintone)
                        putExtra(SKINTYPE, product.skinType)
                        putExtra(UNDERTONE, product.undertone)
                        putExtra(SHADE, product.shade)
                        putExtra(MAKEUPTYPE, product.makeupType)
                    }
                    it.context.startActivity(intent)
                }
            }
        }
    }
}