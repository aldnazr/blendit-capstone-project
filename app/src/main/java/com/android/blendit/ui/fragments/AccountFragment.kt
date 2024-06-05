package com.android.blendit.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.blendit.databinding.FragmentAccountBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.ui.ViewModelFactory
import com.android.blendit.ui.login.LoginActivity
import com.android.blendit.ui.main.MainViewModel

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val accountPreference by lazy { AccountPreference(requireActivity()) }
    private val viewModel by activityViewModels<MainViewModel> {
        ViewModelFactory.getInstance(
            accountPreference
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
        viewModel.loadLoginInfo.observe(viewLifecycleOwner) {
            binding.emailEditText.setText(it.email)
            binding.nameTextView.text = it.username
        }
        binding.logoutButton.setOnClickListener {
            accountPreference.removeLoginUser()
            startActivity(
                Intent(
                    requireActivity(),
                    LoginActivity::class.java
                )
            )
            requireActivity().finishAffinity()
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