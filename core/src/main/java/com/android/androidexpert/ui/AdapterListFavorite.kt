package com.android.androidexpert.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.androidexpert.core.R
import com.android.androidexpert.core.databinding.ItemFavoriteBinding
import com.android.androidexpert.domain.model.ProductFavorite
import com.bumptech.glide.Glide

class AdapterListFavorite(
    val addFavorite: (ProductFavorite) -> Unit,
    val removeFavorite: (ProductFavorite) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableListOf<ProductFavorite>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(listFavorite: List<ProductFavorite>) {
        list.clear()
        list.addAll(listFavorite)
        notifyDataSetChanged()
    }

    inner class NormalViewHolder(val binding: ItemFavoriteBinding) : ViewHolder(binding.root) {

        override fun bindView(ProductFavorite: ProductFavorite) {

            val detailActivity =
                Class.forName("com.android.androidexpert.presentation.detail.DetailActivity")

            binding.productName.text = ProductFavorite.productName
            binding.brand.text = ProductFavorite.brand
            Glide.with(binding.root).load(ProductFavorite.picture).into(binding.imageView)

            with(binding.btnFavorite) {
                isChecked = true
                addOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        addFavorite(ProductFavorite)
                    } else {
                        removeFavorite(ProductFavorite)
                    }
                }
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, detailActivity).apply {
                    putExtra(ID, ProductFavorite.productId)
                    putExtra(BRAND, ProductFavorite.brand)
                    putExtra(PRODUCT_NAME, ProductFavorite.productName)
                    putExtra(PICTURE, ProductFavorite.picture)
                    putExtra(TYPE, ProductFavorite.type)
                    putExtra(SKINTONE, ProductFavorite.skintone)
                    putExtra(SKINTYPE, ProductFavorite.skinType)
                    putExtra(UNDERTONE, ProductFavorite.undertone)
                    putExtra(SHADE, ProductFavorite.shade)
                    putExtra(MAKEUPTYPE, ProductFavorite.makeupType)
                    putExtra(IS_FAVORITE, binding.btnFavorite.isChecked)
                }
                it.context.startActivity(intent)
            }
        }
    }

    inner class FooterViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bindView(ProductFavorite: ProductFavorite) {}
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size) {
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
                ItemFavoriteBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            val data = list[position]
            holder.bindView(data)
        }
    }

    override fun getItemCount(): Int = list.size + 1

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bindView(ProductFavorite: ProductFavorite)
    }

    companion object {
        const val FOOTER_VIEW = 1

        const val ID = "id"
        const val PICTURE = "picture"
        const val BRAND = "brand"
        const val PRODUCT_NAME = "product_name"
        const val IS_FAVORITE = "is_favorite"
        const val TYPE = "type"
        const val SKINTONE = "skintone"
        const val SKINTYPE = "skin_type"
        const val UNDERTONE = "undertone"
        const val SHADE = "shade"
        const val MAKEUPTYPE = "makeup_type"
    }
}