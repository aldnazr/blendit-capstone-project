package com.android.blendit.ui.activity.recommendation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.android.blendit.remote.Result
import com.android.blendit.R
import com.android.blendit.adapter.RecommendationAdapter
import com.android.blendit.databinding.ActivityRecommendationBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.response.RecommendationResult
import com.android.blendit.ui.fragments.FavoriteFragment
import com.android.blendit.ui.recommendation.RecommendationViewModel
import com.android.blendit.viewmodel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding
    private val accountPreference by lazy { AccountPreference(this) }
    private val recommendationViewModel: RecommendationViewModel by viewModels {
        ViewModelFactory.getInstance(accountPreference)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        val adapter = RecommendationAdapter { recommendation ->
            showFavoriteDialog(recommendation)
        }
        binding.rvRecommendation.adapter = adapter

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

        binding.buttonRecommendation.setOnClickListener {
            openFavoriteFragment()
        }

        // Get data from intent and call getRecommendations function
        val loginResult = accountPreference.getLoginInfo()
        val token = loginResult.token
        val skintone = intent.getStringExtra(EXTRA_SKIN_TONE) ?: ""
        val undertone = intent.getStringExtra(EXTRA_UNDERTONE) ?: ""
        val skinType = intent.getStringExtra(EXTRA_SKIN_TYPE) ?: ""

        recommendationViewModel.getRecommendations(token.toString(), skintone, undertone, skinType)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun showFavoriteDialog(recommendation: RecommendationResult) {
        MaterialAlertDialogBuilder(this).setTitle("Add to Favorite")
            .setMessage("Do you want to add ${recommendation.productName} to your favorite?")
            .setPositiveButton("Yes") { _, _ ->
                addFavorite(recommendation)
            }.setNegativeButton("No", null).show()
    }

    private fun openFavoriteFragment() {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, FavoriteFragment())
            addToBackStack(null)
        }
    }

    private fun addFavorite(recommendation: RecommendationResult) {
        val loginResult = accountPreference.getLoginInfo()
        val token = loginResult.token
        val userId = loginResult.userId
        val productId = recommendation.id

        if (token != null && userId != null && productId != null) {
            recommendationViewModel.addFavorite(productId)
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
