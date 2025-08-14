package com.android.androidexpert.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android.androidexpert.R
import com.android.androidexpert.data.source.local.AccountPreference
import com.android.androidexpert.presentation.MainActivity
import com.android.androidexpert.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val launchTime = 2000L
    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setFullscreen()
        launchApp()
    }

    private fun launchApp() {
        lifecycleScope.launch {
            delay(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) 0 else launchTime)
            withContext(Dispatchers.Main) {
                viewModel.getLoginInfo().collect { loginInfo ->
                    val intent = if (loginInfo.token.isNullOrEmpty()) {
                        Intent(this@SplashActivity, LoginActivity::class.java)
                    } else {
                        Intent(this@SplashActivity, MainActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setFullscreen() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}