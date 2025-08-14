package com.android.androidexpert.presentation.register

import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.android.androidexpert.data.ApiResponse
import com.android.androidexpert.databinding.ActivityRegisterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFullScreen()
        setView()
    }

    private fun setFullScreen() {
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

            emailEditText.doAfterTextChanged {
                if (it != null) {
                    emailErrorTextView.visibility = if (Patterns.EMAIL_ADDRESS.matcher(it)
                            .matches() || it.isEmpty()
                    ) View.GONE else View.VISIBLE
                }
            }
            passwordEditText.doAfterTextChanged {
                if (it != null) {
                    passwordErrorTextView.visibility =
                        if (it.length >= 6 || it.isEmpty()) View.GONE else View.VISIBLE
                }
            }
            retypePasswordEditText.doAfterTextChanged {
                if (it != null) {
                    retypePasswordErrorTextView.visibility =
                        if (it.toString() == passwordEditText.text.toString() || it.isEmpty()) View.GONE else View.VISIBLE
                }
            }
        }
    }

    private fun checkForm() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val retypePassword = binding.retypePasswordEditText.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && retypePassword.isNotEmpty()) {
            registeringAccount(name, email, password)
        }
    }

    private fun registeringAccount(name: String, email: String, password: String) {
        viewModel.userRegister(name, email, password)
            .observe(this@RegisterActivity) { result ->
                if (result != null) {
                    when (result) {
                        is ApiResponse.Loading -> {
                            showLoading(true)
                        }

                        is ApiResponse.Error -> {
                            showLoading(false)
                            showDialog(
                                "Pendaftaran gagal", "Harap coba kembali"
                            ) { it.dismiss() }
                        }

                        is ApiResponse.Success -> {
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
        binding.nameEditText.text?.clear()
        binding.emailEditText.text?.clear()
        binding.passwordEditText.text?.clear()
        binding.retypePasswordEditText.text?.clear()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}