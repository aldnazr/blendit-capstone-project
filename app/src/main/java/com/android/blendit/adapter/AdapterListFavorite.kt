package com.android.blendit.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.databinding.FavoriteItemBinding
import com.android.blendit.remote.response.ItemsFavorite
import com.android.blendit.ui.activity.detailitem.DetailActivity
import com.bumptech.glide.Glide

class AdapterListFavorite() : RecyclerView.Adapter<AdapterListFavorite.ViewHolder>() {

    private val list = mutableListOf<ItemsFavorite>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(listFavorite: List<ItemsFavorite>) {
        list.clear()
        list.addAll(listFavorite)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setView(itemsFavorite: ItemsFavorite) {
            binding.productName.text = itemsFavorite.productName
            binding.brand.text = itemsFavorite.brand
            Glide.with(binding.root).load(itemsFavorite.picture).into(binding.imageView)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.ID, itemsFavorite.productId)
                    putExtra(DetailActivity.BRAND, itemsFavorite.brand)
                    putExtra(DetailActivity.PRODUCT_NAME, itemsFavorite.productName)
                    putExtra(DetailActivity.PICTURE, itemsFavorite.picture)
                    putExtra(DetailActivity.IS_FAVORITE, true)
                }
                it.context.startActivity(intent)
            }
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