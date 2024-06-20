package com.android.blendit.ui.activity.recommendation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.android.blendit.R
import com.android.blendit.adapter.RecommendationAdapter
import com.android.blendit.databinding.ActivityRecommendationBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.Result
import com.android.blendit.remote.response.RecommendationResult
import com.android.blendit.ui.fragments.FavoriteFragment
import com.android.blendit.ui.recommendation.RecommendationViewModel
import com.android.blendit.viewmodel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RecommendationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRecommendationBinding.inflate(layoutInflater) }
    private val accountPreference by lazy { AccountPreference(this) }
    val adapter = RecommendationAdapter()
    private val recommendationViewModel: RecommendationViewModel by viewModels {
        ViewModelFactory.getInstance(accountPreference)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Setup RecyclerView
        fetchListFavorite()
        observeRecommendation()
        getRecommendation()
        setView()
    }

    private fun setView() {
        binding.rvRecommendation.adapter = adapter
        binding.buttonRecommendation.setOnClickListener { openFavoriteFragment() }
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun getRecommendation() {
        // Get data from intent and call getRecommendations function
        val loginResult = accountPreference.getLoginInfo()
        val token = loginResult.token
        val skintone = intent.getStringExtra(EXTRA_SKIN_TONE) ?: ""
        val undertone = intent.getStringExtra(EXTRA_UNDERTONE) ?: ""
        val skinType = intent.getStringExtra(EXTRA_SKIN_TYPE) ?: ""

        recommendationViewModel.getRecommendations(token.toString(), skintone, undertone, skinType)
    }

    private fun observeRecommendation() {
        // Observe recommendation results
        recommendationViewModel.recommendationResult.observe(this) { recommendations ->
            recommendations?.let {
                Log.d("RecommendationActivity", "Recommendations received: $recommendations")
                adapter.submitList(recommendations)
            }
        }

        // Observe error messages
        recommendationViewModel.errorMessage.observe(this) { message ->
            message?.let {
                showToast(it)
                Log.e("RecommendationActivity", "Error message: $it")
            }
        }

        // Observe favorite response
        recommendationViewModel.favoriteResponse.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading if needed
                }

                is Result.Success -> {
                    showToast("Added to favorite")
                }

                is Result.Error -> {
                    showToast("Failed to add to favorite: ")
                }
            }
        }
    }

    private fun fetchListFavorite() {
        recommendationViewModel.getListFavorite().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                }

                is Result.Error -> {
                }

                is Result.Success -> {
                    adapter.setList(result.data)
                }
            }
        }
    }

    private fun openFavoriteFragment() {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, FavoriteFragment())
            addToBackStack(null)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_SKIN_TONE = "EXTRA_SKIN_TONE"
        const val EXTRA_UNDERTONE = "EXTRA_UNDERTONE"
        const val EXTRA_SKIN_TYPE = "EXTRA_SKIN_TYPE"
    }
}
