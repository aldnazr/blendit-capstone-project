package com.android.blendit.ui.analysis

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.blendit.R
import com.android.blendit.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    companion object {
        const val EXTRA_FACE_TYPE = "EXTRA_FACE_TYPE"
        const val EXTRA_SKIN_TONE = "EXTRA_SKIN_TONE"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val faceType = intent.getStringExtra(EXTRA_FACE_TYPE)
        val skinTone = intent.getStringExtra(EXTRA_SKIN_TONE)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)

        binding.tvFaceType.text = faceType
        binding.tvSkinTone.text = skinTone
        binding.tvDescription.text = description

        binding.buttonResult.setOnClickListener {
            // Navigate to tutorial or any other activity
        }
    }
}