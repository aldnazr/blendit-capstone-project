package com.android.blendit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.R
import com.android.blendit.adapter.AdapterListFavorite.NormalViewHolder
import com.android.blendit.adapter.AdapterListFavorite.ViewHolder
import com.android.blendit.databinding.ItemCategoryBinding
import com.android.blendit.remote.response.ListCategory
import com.bumptech.glide.Glide

class CategoryAdapter(
    private val categories: List<ListCategory>, private val onItemClick: (ListCategory) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class NormalViewHolder(private val binding: ItemCategoryBinding) :
        ViewHolder(binding.root) {

        override fun bindView(listCategory: ListCategory) {
            binding.tvCategory.text = listCategory.category
            Glide.with(binding.root.context).load(listCategory.thumbnail).into(binding.ivCategory)

            binding.root.setOnClickListener {
                onItemClick(listCategory)
            }
        }
    }

    inner class FooterViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bindView(listCategory: ListCategory) {}
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == categories.size) {
            FOOTER_VIEW
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == FOOTER_VIEW) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.footer_layout, parent, false)
            FooterViewHolder(view)
        } else {
            NormalViewHolder(
                ItemCategoryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            val data = categories[position]
            holder.bindView(data)
        }
    }

    override fun getItemCount(): Int {
        return categories.size + 1
    }

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bindView(listCategory: ListCategory)
    }

    companion object {
        const val FOOTER_VIEW = 1
    }
}
