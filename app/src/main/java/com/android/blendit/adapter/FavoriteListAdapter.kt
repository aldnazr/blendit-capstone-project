package com.android.blendit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.data.ProductItem
import com.android.blendit.databinding.ProductItemBinding

class FavoriteListAdapter(private val list: MutableList<ProductItem>) :
    RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setView(productItem: ProductItem) {
            binding.title.text = productItem.title
            binding.description.text = productItem.desc
            binding.imageView.setImageDrawable(productItem.img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.setView(data)
    }
}