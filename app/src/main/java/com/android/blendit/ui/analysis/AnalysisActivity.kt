package com.android.blendit.ui.analysis

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.blendit.R
import com.android.blendit.data.api.ApiConfig
import com.android.blendit.data.api.ImageAnalysisRequest
import com.android.blendit.data.api.ImageAnalysisResponse
import com.android.blendit.databinding.ActivityAnalysisBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalysisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pictureUriString = intent.getStringExtra("pictureUri")
        val pictureUri = Uri.parse(pictureUriString)
        binding.previewImageView.setImageURI(pictureUri)

        binding.buttonAnalys.setOnClickListener { analyzeImage(pictureUriString) }


    }

    private fun analyzeImage(imageUri: String?) {
        if (imageUri != null) {
            val request = ImageAnalysisRequest(imageUri)
            val apiService = ApiConfig().getApiService()
            apiService.analyzeImage(request).enqueue(object : Callback<ImageAnalysisResponse> {
                override fun onResponse(call: Call<ImageAnalysisResponse>, response: Response<ImageAnalysisResponse>) {
                    if (response.isSuccessful) {
                        val analysisResult = response.body()
                        val intent = Intent(this@AnalysisActivity, ResultActivity::class.java).apply {
                            putExtra(ResultActivity.EXTRA_FACE_TYPE, analysisResult?.faceType)
                            putExtra(ResultActivity.EXTRA_SKIN_TONE, analysisResult?.skinTone)
                            putExtra(ResultActivity.EXTRA_DESCRIPTION, analysisResult?.description)
                        }
                        startActivity(intent)
                    } else {
                        showToast("Analysis failed. Please try again.")
                    }
                }

                override fun onFailure(call: Call<ImageAnalysisResponse>, t: Throwable) {
                    showToast("Failed to connect to the server.")
                }
            })
        } else {
            showToast("No image selected")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}