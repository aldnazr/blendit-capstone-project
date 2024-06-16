package com.android.blendit.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.databinding.FavoriteItemBinding
import com.android.blendit.remote.response.ItemsFavorite
import com.bumptech.glide.Glide

class AdapterListFavorite() :
    RecyclerView.Adapter<AdapterListFavorite.ViewHolder>() {

    private val list = mutableListOf<ItemsFavorite>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(listFavorite: List<ItemsFavorite>) {
        list.addAll(listFavorite)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setView(itemsFavorite: ItemsFavorite) {
            binding.brand.text = itemsFavorite.brand
            binding.productName.text = itemsFavorite.productName
            Glide.with(binding.root).load(itemsFavorite.picture).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FavoriteItemBinding.inflate(
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