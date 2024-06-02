package com.android.blendit.ui.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.blendit.R
import com.android.blendit.databinding.ActivityLoginBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.Result
import com.android.blendit.remote.response.LoginResult
import com.android.blendit.remote.response.ResponseLogin
import com.android.blendit.ui.ViewModelFactory
import com.android.blendit.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {

    private val loginPreferences by lazy { AccountPreference(this) }
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(
            loginPreferences
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setFullScreen()
        binding.loginBtn.setOnClickListener { processLogin() }
    }

    private fun processLogin() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passEditText.text.toString()

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotEmpty()) {
            checkLogin(email, password)
        } else {
            showAlert(
                "Login gagal", "Harap masukkan email dan sandi dengan benar"
            ) { }
        }
    }

    private fun checkLogin(email: String, password: String) {
        loginViewModel.userLogin(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showAlert(
                            "Login gagal", "Email atau sandi salah"
                        ) { }
                    }

                    is Result.Success -> {
                        showLoading(false)
                        saveLoginData(result.data)
                        navigateToMain()
                    }
                }
            }
        }
    }

    private fun showAlert(
        title: String, message: String, positiveAction: (dialog: DialogInterface) -> Unit
    ) {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                positiveAction.invoke(dialog)
            }
            setCancelable(false)
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun saveLoginData(loginResponse: ResponseLogin) {
        val accountPreference = AccountPreference(this)
        val loginResult = loginResponse.loginResult
        val accountLogin = LoginResult(
            username = loginResult.username,
            userId = loginResult.userId,
            token = loginResult.token,
            email = loginResult.email,
            profilePic = loginResult.profilePic
        )
        accountPreference.setLoginInfo(accountLogin)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setFullScreen() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}