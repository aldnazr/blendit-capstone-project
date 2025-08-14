package com.android.androidexpert.presentation.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.androidexpert.data.ApiResponse
import com.android.androidexpert.databinding.FragmentAccountBinding
import com.android.androidexpert.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel by activityViewModels<AccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFullscreen()
        setView()
    }

    private fun setView() {
        with(binding) {
            viewModel.loginInfo().observe(viewLifecycleOwner) { loginInfo ->
                nameTextView.text = loginInfo.username
                emailTextView.text = loginInfo.email
            }
            btnLogout.setOnClickListener {
                viewModel.removeLoginUser()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finishAffinity()
            }
            btnSave.setOnClickListener {
                if (tvPass.visibility == View.GONE && tvRetypePass.visibility == View.GONE) {
                    changePass()
                }
            }
            tfPassword.doAfterTextChanged {
                val pass = tfPassword.text.toString()
                tvPass.visibility =
                    if (pass.length >= 6 || pass.isEmpty()) View.GONE else View.VISIBLE
            }
            tfRetypePass.doAfterTextChanged {
                val pass = tfPassword.text.toString()
                val retypePass = binding.tfRetypePass.text.toString()
                tvRetypePass.visibility =
                    if (pass == retypePass || retypePass.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    private fun changePass() {
        val pass = binding.tfPassword.text.toString()
        viewModel.changePass(pass).observe(viewLifecycleOwner) { result ->
            if (result is ApiResponse.Success) {
                Toast.makeText(requireActivity(), result.data.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setFullscreen() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.appBar.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }
}