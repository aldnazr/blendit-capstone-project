package com.android.androidexpert.presentation.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android.androidexpert.R
import com.android.androidexpert.databinding.ActivityDetailBinding
import com.android.androidexpert.domain.model.ProductFavorite
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFullScreen()
        setView()
    }

    @SuppressLint("StringFormatInvalid", "SetTextI18n")
    private fun setView() {
        val productFavorite = ProductFavorite(
            productId = intent.getStringExtra(ID).orEmpty(),
            brand = intent.getStringExtra(BRAND).orEmpty(),
            productName = intent.getStringExtra(PRODUCT_NAME).orEmpty(),
            picture = intent.getStringExtra(PICTURE).orEmpty(),
            type = intent.getStringExtra(TYPE).orEmpty(),
            skintone = intent.getStringExtra(SKINTONE).orEmpty(),
            skinType = intent.getStringExtra(SKINTYPE).orEmpty(),
            undertone = intent.getStringExtra(UNDERTONE).orEmpty(),
            shade = intent.getStringExtra(SHADE).orEmpty(),
            makeupType = intent.getStringExtra(MAKEUPTYPE).orEmpty()
        )

        with(binding) {
            Glide.with(this@DetailActivity).load(productFavorite.picture).into(ivPicture)
            tvTitle.text = productFavorite.brand
            tvProductName.text = productFavorite.productName
            tvType.text = "${getString(R.string.product_type)} ${productFavorite.type}"
            tvSkinTone.text = "${getString(R.string.product_skintone)} ${productFavorite.skintone}"
            tvSkinType.text = "${getString(R.string.product_skintype)} ${productFavorite.skinType}"
            tvUndertone.text =
                "${getString(R.string.product_undertone)} ${productFavorite.undertone}"
            tvShade.text = "${getString(R.string.product_shade)} ${productFavorite.shade}"
            tvMakeupType.text =
                "${getString(R.string.product_makeup_type)} ${productFavorite.makeupType}"

            btnBack.setOnClickListener { finish() }
            lifecycleScope.launch {
                val isFavorite = viewModel.isFavorite(productFavorite.productId)
                btnFavorite.isChecked = isFavorite
            }

            btnFavorite.addOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.addFavorite(productFavorite)
                } else {
                    viewModel.removeFavorite(productFavorite)
                }
            }
        }
    }

    private fun setFullScreen() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            binding.appBar.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

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
    }
}