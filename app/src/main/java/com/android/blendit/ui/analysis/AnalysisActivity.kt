package com.android.blendit.ui.analysis

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.blendit.R
import com.android.blendit.databinding.ActivityAnalysisBinding
import com.android.blendit.preference.AccountPreference

class AnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalysisBinding
    private val analysisViewModel: AnalysisViewModel by viewModels()
    private lateinit var accountPreference: AccountPreference

    private val predefinedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InVzZXItRXBPY0hNbTFtSTQ9IiwiaWF0IjoxNzE4NTQxNjc5fQ.7y5qO635p8HObHbWE1HleWxhrfaX_vrq8OY2k5QT_Ks"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        accountPreference = AccountPreference(this)

        val pictureUriString = intent.getStringExtra("pictureUri")
        val pictureUri = Uri.parse(pictureUriString)
        binding.previewImageView.setImageURI(pictureUri)

        binding.buttonAnalys.setOnClickListener { showInputDialog(pictureUriString) }

        analysisViewModel.analysisResult.observe(this, Observer { result ->
            result?.let {
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra(ResultActivity.EXTRA_FACE_TYPE, it.face_type)
                    putExtra(ResultActivity.EXTRA_SKIN_TONE, it.skintone)
                    putExtra(ResultActivity.EXTRA_UNDERTONE, it.undertone)
                    putExtra(ResultActivity.EXTRA_SKIN_TYPE, it.skin_type)
                    putExtra(ResultActivity.EXTRA_DESCRIPTION, it.description)
                }
                startActivity(intent)
            }
        })

        analysisViewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                showToast(it)
            }
        })

    }

    private fun showInputDialog(imageUri: String?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_input_analysis, null)
        val skinToneSpinner = dialogView.findViewById<Spinner>(R.id.spinner_skin_tone)
        val undertoneSpinner = dialogView.findViewById<Spinner>(R.id.spinner_undertone)
        val skinTypeSpinner = dialogView.findViewById<Spinner>(R.id.spinner_skin_type)

        ArrayAdapter.createFromResource(
            this,
            R.array.skin_tone_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            skinToneSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.undertone_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            undertoneSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.skin_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            skinTypeSpinner.adapter = adapter
        }

        AlertDialog.Builder(this)
            .setTitle("Enter Details")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val skinTone = skinToneSpinner.selectedItem.toString()
                val undertone = undertoneSpinner.selectedItem.toString()
                val skinType = skinTypeSpinner.selectedItem.toString()
                analyzeImage(imageUri, skinTone, undertone, skinType)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun analyzeImage(imageUri: String?, skinTone: String, undertone: String, skinType: String) {
        if (imageUri != null) {
            val loginResult = accountPreference.getLoginInfo()
            val token = loginResult.token
            if (token != null) {
                analysisViewModel.analyzeImage(token, imageUri, skinTone, undertone, skinType)
            } else {
                showToast("Token not found")
            }
        } else {
            showToast("No image selected")
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}