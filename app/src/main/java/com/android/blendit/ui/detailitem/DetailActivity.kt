package com.android.blendit.ui.detailitem

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.blendit.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    val intentPicture by lazy { intent.getStringExtra(PICTURE) }
    val intentBrand by lazy { intent.getStringExtra(BRAND) }
    val intentProductName by lazy { intent.getStringExtra(PRODUCT_NAME) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFullScreen()
        setView()
    }

    private fun setView() {
        Glide.with(this).load(intentPicture).into(binding.ivPicture)
        binding.tvBrand.text = intentBrand
        binding.tvProductName.text = intentProductName
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setFullScreen() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        const val PICTURE = "picture"
        const val BRAND = "brand"
        const val PRODUCT_NAME = "product_name"
    }
}