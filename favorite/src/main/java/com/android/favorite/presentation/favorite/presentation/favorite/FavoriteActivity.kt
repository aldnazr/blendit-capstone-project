package com.android.favorite.presentation.favorite.presentation.favorite

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.androidexpert.di.FavoriteModuleDependencies
import com.android.androidexpert.ui.AdapterListFavorite
import com.android.favorite.databinding.ActivityFavoriteBinding
import com.android.favorite.di.DaggerFavoriteComponent
import com.android.favorite.presentation.favorite.presentation.viewmodel.FavoriteViewModelFactory
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFavoriteBinding.inflate(layoutInflater) }
    private lateinit var adapter: AdapterListFavorite

    @Inject
    lateinit var viewModelFactory: FavoriteViewModelFactory

    private val viewModel by viewModels<FavoriteViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        DaggerFavoriteComponent.builder().context(this).appDependencies(
            EntryPointAccessors.fromApplication(
                applicationContext, FavoriteModuleDependencies::class.java
            )
        ).build().inject(this)

        adapter = AdapterListFavorite(addFavorite = { viewModel.addFavorite(it) },
            removeFavorite = { viewModel.removeFavorite(it) })
        setFullscreen()
        setView()
    }

    override fun onResume() {
        super.onResume()
        setListFavoriteProduct()
    }

    private fun setView() {
        with(binding) {
            recyclerView.adapter = adapter
            swipeRefresh.setOnRefreshListener { setListFavoriteProduct() }
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener { finish() }
        }
    }

    private fun setListFavoriteProduct() {
        viewModel.listFavorite().observe(this@FavoriteActivity) { result ->
            showLoading(true)
            val isDataAvailable = if (result.isEmpty()) View.VISIBLE else View.GONE
            binding.tvEmpty.visibility = isDataAvailable
            binding.animEmpty.visibility = isDataAvailable
            adapter.setList(result)
            CoroutineScope(Dispatchers.Main).launch {
                delay(200L)
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.swipeRefresh.isRefreshing = isLoading
    }

    private fun setFullscreen() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.appBar.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

}