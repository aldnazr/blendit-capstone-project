package com.android.blendit.ui.recommendation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.blendit.remote.Result
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.blendit.R
import com.android.blendit.databinding.ActivityRecommendationBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.response.RecommendationResult
import com.android.blendit.remote.retrofit.ApiConfig
import com.android.blendit.ui.ViewModelFactory
import com.android.blendit.ui.fragments.FavoriteFragment
import com.android.blendit.ui.login.LoginViewModel
import com.android.blendit.ui.tutorial.TutorialActivity
import com.android.blendit.viewmodel.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Callback
import retrofit2.Response
class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding
    private val recommendationViewModel: RecommendationViewModel by viewModels {
        ViewModelFactory.getInstance(accountPreference)
    }

    private lateinit var accountPreference: AccountPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accountPreference = AccountPreference(this)

        // Setup RecyclerView
        binding.rvRecommendation.layoutManager = LinearLayoutManager(this)
        val adapter = RecommendationAdapter { recommendation ->
            showFavoriteDialog(recommendation)
        }
        binding.rvRecommendation.adapter = adapter

        // Observe recommendation results
        recommendationViewModel.recommendationResult.observe(this, Observer { recommendations ->
            recommendations?.let {
                Log.d("RecommendationActivity", "Recommendations received: $recommendations")
                adapter.submitList(recommendations)
            }
        })

        // Observe error messages
        recommendationViewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                showToast(it)
                Log.e("RecommendationActivity", "Error message: $it")
            }
        })

        // Observe favorite response
        recommendationViewModel.favoriteResponse.observe(this, Observer { result ->
            when (result) {
                is com.android.blendit.remote.Result.Loading -> {
                    // Show loading if needed
                }
                is com.android.blendit.remote.Result.Success -> {
                    showToast("Added to favorite")
                }
                is com.android.blendit.remote.Result.Error -> {
                    showToast("Failed to add to favorite: ")
                }
            }
        })

        binding.buttonRecommendation.setOnClickListener {
            val favoriteIntent = Intent(this, FavoriteFragment::class.java)
            startActivity(favoriteIntent)
        }

        // Get data from intent and call getRecommendations function
        val loginResult = accountPreference.getLoginInfo()
        val token = loginResult.token
        val skintone = intent.getStringExtra(EXTRA_SKIN_TONE) ?: ""
        val undertone = intent.getStringExtra(EXTRA_UNDERTONE) ?: ""
        val skinType = intent.getStringExtra(EXTRA_SKIN_TYPE) ?: ""

        recommendationViewModel.getRecommendations(token.toString(), skintone, undertone, skinType)
    }

    private fun showFavoriteDialog(recommendation: RecommendationResult) {
        AlertDialog.Builder(this)
            .setTitle("Add to Favorite")
            .setMessage("Do you want to add ${recommendation.productName} to your favorite?")
            .setPositiveButton("Yes") { _, _ ->
                addFavorite(recommendation)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun addFavorite(recommendation: RecommendationResult) {
        val loginResult = accountPreference.getLoginInfo()
        val token = loginResult.token
        val userId = loginResult.userId
        val productId = recommendation.id

        if (token != null && userId != null && productId != null) {
            recommendationViewModel.addFavorite(token, userId, productId)
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
