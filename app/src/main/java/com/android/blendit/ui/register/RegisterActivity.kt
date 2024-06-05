package com.android.blendit.ui.register

import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.blendit.R
import com.android.blendit.databinding.ActivityRegisterBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.Result
import com.android.blendit.ui.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterActivity : AppCompatActivity() {

    private val loginPreferences by lazy { AccountPreference(this) }
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(
            loginPreferences
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFullScreen()
        setView()
    }

    private fun setFullScreen() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setView() {
        with(binding) {
            registerButton.setOnClickListener { checkForm() }
            toLoginPage.setOnClickListener { finish() }
        }
    }

    private fun checkForm() {
        val username = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val retypePassword = binding.retypePasswordEditText.text.toString()

        if (username.isEmpty()) {
            Toast.makeText(this, "Masukkan nama", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Sandi kurang dari 8", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != retypePassword) {
            Toast.makeText(this, "Sandi tidak sama", Toast.LENGTH_SHORT).show()
            return
        }

        registeringAccount(username, email, password)
    }

    private fun registeringAccount(userName: String, email: String, password: String) {
        registerViewModel.userRegister(userName, email, password)
            .observe(this@RegisterActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showDialog(
                                "Pendaftaran gagal", "Harap coba kembali"
                            ) { it.dismiss() }
                        }

                        is Result.Success -> {
                            showLoading(false)
                            registerSuccess()
                        }
                    }
                }
            }
    }

    private fun showDialog(
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

    private fun registerSuccess() {
        showDialog(
            "Sukses", "Pendaftaran berhasil, silahkan login"
        ) { finish() }
        binding.usernameEditText.text?.clear()
        binding.emailEditText.text?.clear()
        binding.passwordEditText.text?.clear()
        binding.retypePasswordEditText.text?.clear()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}