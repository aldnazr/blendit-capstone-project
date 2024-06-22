package com.android.blendit.ui.activity.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.blendit.R
import com.android.blendit.databinding.ActivityDetailBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val accountPreference by lazy { AccountPreference(this) }
    private val detailViewModel by viewModels<DetailItemViewModel> {
        ViewModelFactory(
            accountPreference
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFullScreen()
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView() {
        val intentId = intent.getStringExtra(ID)
        val intentPicture = intent.getStringExtra(PICTURE)
        val intentBrand = intent.getStringExtra(BRAND)
        val intentProductName = intent.getStringExtra(PRODUCT_NAME)
        val intentType = intent.getStringExtra(TYPE)
        val intentSkinTone = intent.getStringExtra(SKINTONE)
        val intentSkinType = intent.getStringExtra(SKINTYPE)
        val intentUnderTone = intent.getStringExtra(UNDERTONE)
        val intentShade = intent.getStringExtra(SHADE)
        val intentMakeupType = intent.getStringExtra(MAKEUPTYPE)
        val intentIsFavorite = intent.getBooleanExtra(IS_FAVORITE, false)

        with(binding) {
            Glide.with(this@DetailActivity).load(intentPicture).into(ivPicture)
            tvTitle.text = intentBrand
            tvProductName.text = intentProductName
            tvType.text = "${getString(R.string.product_type)} $intentType"
            tvSkinTone.text = "${getString(R.string.product_skintone)} $intentSkinTone"
            tvSkinType.text = "${getString(R.string.product_skintype)} $intentSkinType"
            tvUndertone.text = "${getString(R.string.product_undertone)} $intentUnderTone"
            tvShade.text = "${getString(R.string.product_shade)} $intentShade"
            tvMakeupType.text = "${getString(R.string.product_makeup_type)} $intentMakeupType"
            btnBack.setOnClickListener { finish() }
            btnFavorite.isChecked = intentIsFavorite
            btnFavorite.addOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    detailViewModel.addFavorite(intentId.toString())
                } else {
                    detailViewModel.removeFavorite(intentId.toString())
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
        const val IS_FAVORITE = "is_favorite"
        const val TYPE = "type"
        const val SKINTONE = "skintone"
        const val SKINTYPE = "skin_type"
        const val UNDERTONE = "undertone"
        const val SHADE = "shade"
        const val MAKEUPTYPE = "makeup_type"
    }
}