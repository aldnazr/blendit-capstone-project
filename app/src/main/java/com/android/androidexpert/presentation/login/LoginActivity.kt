package com.android.androidexpert.presentation.login

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.android.androidexpert.data.ApiResponse
import com.android.androidexpert.databinding.ActivityLoginBinding
import com.android.androidexpert.domain.model.LoginResult
import com.android.androidexpert.domain.model.UserAccount
import com.android.androidexpert.presentation.MainActivity
import com.android.androidexpert.presentation.register.RegisterActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFullScreen()
        setView()
    }

    private fun setView() {
        with(binding) {
            emailEditText.doAfterTextChanged {
                if (it != null) {
                    emailErrorTextView.visibility = if (Patterns.EMAIL_ADDRESS.matcher(it)
                            .matches() || it.isEmpty()
                    ) View.GONE else View.VISIBLE
                }
            }
            passEditText.doAfterTextChanged {
                if (it != null) {
                    passwordErrorTextView.visibility =
                        if (it.length >= 6 || it.isEmpty()) View.GONE else View.VISIBLE
                }
            }
            loginBtn.setOnClickListener { processLogin() }
            textCreateAccount.setOnClickListener {
                startActivity(
                    Intent(
                        this@LoginActivity, RegisterActivity::class.java
                    )
                )
            }
        }
    }

    private fun checkSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "default_user_id")
        val email = sharedPreferences.getString("email", "default_email")
        val username = sharedPreferences.getString("username", "default_username")
        val token = sharedPreferences.getString("token", "default_token")
        val profilePic = sharedPreferences.getString("profilepic", "default_profilepic")

        Log.d("SharedPrefsCheck", "UserId: $userId")
        Log.d("SharedPrefsCheck", "Email: $email")
        Log.d("SharedPrefsCheck", "Username: $username")
        Log.d("SharedPrefsCheck", "Token: $token")
        Log.d("SharedPrefsCheck", "ProfilePic: $profilePic")
    }

    private fun processLogin() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passEditText.text.toString()

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotEmpty()) {
            checkLogin(email, password)
        } else {
            showAlert(
                "Harap masukkan email dan sandi dengan benar"
            ) { it.dismiss() }
        }
    }

    private fun checkLogin(email: String, password: String) {
        viewModel.getLoginInfo(email, password).observe(this@LoginActivity) { result ->
            if (result != null) {
                when (result) {
                    is ApiResponse.Loading -> {
                        showLoading(true)
                    }

                    is ApiResponse.Error -> {
                        showLoading(false)
                        showAlert("Email atau sandi salah") { it.dismiss() }
                    }

                    is ApiResponse.Success -> {
                        showLoading(false)
                        saveLoginData(result.data)
                        navigateToMain()
                    }
                }
            }
        }
    }

    private fun showAlert(
        message: String, positiveAction: (dialog: DialogInterface) -> Unit
    ) {
        MaterialAlertDialogBuilder(this).apply {
            setTitle("Login gagal")
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

    private fun saveLoginData(loginResult: LoginResult) {
        val userAccount = loginResult.userAccount
        val accountLogin = UserAccount(
            username = userAccount.username,
            userId = userAccount.userId,
            token = userAccount.token,
            email = userAccount.email,
            profilePic = userAccount.profilePic
        )
        viewModel.setLoginInfo(accountLogin)
    }

    private fun navigateToMain() {
        checkSharedPreferences(this@LoginActivity)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setFullScreen() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}