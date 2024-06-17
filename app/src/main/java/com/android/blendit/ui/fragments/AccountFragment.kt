package com.android.blendit.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.blendit.R
import com.android.blendit.databinding.FragmentAccountBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.ui.ViewModelFactory
import com.android.blendit.ui.login.LoginActivity
import com.android.blendit.ui.main.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import java.io.File

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val accountPreference by lazy { AccountPreference(requireActivity()) }
    private var photoFile: File? = null
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
            binding.nameTextView.text = it.username
            binding.emailTextView.text = it.email
        }
        binding.btnLogout.setOnClickListener {
            accountPreference.removeLoginUser()
            startActivity(
                Intent(
                    requireActivity(),
                    LoginActivity::class.java
                )
            )
            requireActivity().finishAffinity()
        }
        binding.btnEditPicture.setOnClickListener { showBottomSheetDialog() }
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheetDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_dialog, null)
        BottomSheetDialog(requireContext()).apply {
            setContentView(dialogView)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            val btnGallery = dialogView.findViewById<MaterialCardView>(R.id.cardViewGallery)
            val btnDelete = dialogView.findViewById<MaterialCardView>(R.id.cardViewDelete)
            btnGallery.setOnClickListener { }
            btnDelete.setOnClickListener { }
            show()
        }
    }

    private fun setFullscreen() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.appBar.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    private fun pickGalleryImg() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherGallery.launch(chooser)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            photoFile = uriToFile(selectedImg, this)
            addStoryModel.setImage(selectedImg)
        }
    }
}