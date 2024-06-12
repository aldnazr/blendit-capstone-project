package com.android.blendit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.databinding.ItemCarouselBinding
import com.bumptech.glide.Glide

class CarouselAdapter : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    val list = mutableListOf<String>()

    class ViewHolder(private val binding: ItemCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setCarouselItem(item: String) {
            Glide.with(itemView.context)
                .load(item)
                .into(binding.listItemImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setCarouselItem(list[position])
    }
}