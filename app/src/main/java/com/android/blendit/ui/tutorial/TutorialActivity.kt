package com.android.blendit.ui.tutorial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.blendit.databinding.ActivityTutorialBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.response.TutorialResult
import com.android.blendit.ui.recommendation.RecommendationActivity
import com.bumptech.glide.Glide

class TutorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTutorialBinding
    private val tutorialViewModel: TutorialViewModel by viewModels()
    private lateinit var accountPreference: AccountPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accountPreference = AccountPreference(this)

        // Observe tutorial result
        tutorialViewModel.tutorialResult.observe(this, Observer { tutorial ->
            tutorial?.let {
                Log.d("TutorialActivity", "Tutorial received: $tutorial")
                updateUIWithTutorial(tutorial)
            }
        })

        // Observe error messages
        tutorialViewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                showToast(it)
                Log.e("TutorialActivity", "Error message: $it")
            }
        })

        // Get data from intent and call getTutorials function
        val loginResult = accountPreference.getLoginInfo()
        val token = loginResult.token
        val shape = intent.getStringExtra(EXTRA_FACE_TYPE) ?: ""
        val skinTone = intent.getStringExtra(EXTRA_SKIN_TONE) ?: ""
        val undertone = intent.getStringExtra(EXTRA_UNDERTONE) ?: ""
        val skinType = intent.getStringExtra(EXTRA_SKIN_TYPE) ?: ""

        tutorialViewModel.getTutorials(token.toString(), shape, skinTone, undertone, skinType)

        // Set OnClickListener for button_tutorial
        binding.buttonTutorial.setOnClickListener {
            navigateToRecommendationActivity(skinTone, undertone, skinType)
        }
    }

    private fun updateUIWithTutorial(tutorial: TutorialResult) {
        // Update UI elements with tutorial data
        binding.tvSkinPreparation.text = tutorial.skinPreparation ?: ""
        binding.tvBaseMakeup.text = tutorial.baseMakeup ?: ""
        binding.tvEyeMakeup.text = tutorial.eyeMakeup ?: ""
        binding.tvLipstik.text = tutorial.shadeLipstik ?: ""

        // Example of loading images using Glide library
        tutorial.imageBase?.let { imageUrl ->
            loadImage(imageUrl, binding.ivBaseMakeup)
        }
        tutorial.imageEye?.let { imageUrl ->
            loadImage(imageUrl, binding.ivEyeMakeup)
        }
        tutorial.imageLips?.let { imageUrl ->
            loadImage(imageUrl, binding.ivLipstik)
        }
    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToRecommendationActivity(skinTone: String, undertone: String, skinType: String) {
        val intent = Intent(this, RecommendationActivity::class.java).apply {
            putExtra(RecommendationActivity.EXTRA_SKIN_TONE, skinTone)
            putExtra(RecommendationActivity.EXTRA_UNDERTONE, undertone)
            putExtra(RecommendationActivity.EXTRA_SKIN_TYPE, skinType)
        }
        startActivity(intent)
    }

    companion object {
        const val EXTRA_FACE_TYPE = "EXTRA_FACE_TYPE"
        const val EXTRA_SKIN_TONE = "EXTRA_SKIN_TONE"
        const val EXTRA_UNDERTONE = "EXTRA_UNDERTONE"
        const val EXTRA_SKIN_TYPE = "EXTRA_SKIN_TYPE"
    }
}