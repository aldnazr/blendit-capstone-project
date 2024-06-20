package com.android.blendit.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.blendit.R
import com.android.blendit.databinding.FragmentAccountBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.ui.activity.login.LoginActivity
import com.android.blendit.ui.activity.main.MainViewModel
import com.android.blendit.utils.convertImageToMultipart
import com.android.blendit.utils.uriToFile
import com.android.blendit.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import java.io.File

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val accountPreference by lazy { AccountPreference(requireActivity()) }
    private val viewModel by activityViewModels<MainViewModel> {
        ViewModelFactory.getInstance(accountPreference)
    }

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
        viewModel.loginInfo.observe(viewLifecycleOwner) { loginInfo ->
            binding.nameTextView.text = loginInfo.username
            binding.emailTextView.text = loginInfo.email
            if (loginInfo.profilePic != null) {
                Glide.with(requireActivity()).load(loginInfo.profilePic)
                    .into(binding.shapeableImageView)
            } else {
                binding.shapeableImageView.setImageDrawable(requireContext().getDrawable(R.drawable.ic_account_fill))
            }
        }
        binding.btnLogout.setOnClickListener {
            accountPreference.removeLoginUser()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }
        binding.btnEditPicture.setOnClickListener { showBottomSheetDialog() }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetView =
            LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_dialog, null)
        val bottomDialog = BottomSheetDialog(requireContext())
        bottomDialog.setContentView(bottomSheetView)
        bottomDialog.setCancelable(true)
        bottomDialog.setCanceledOnTouchOutside(true)
        val btnGallery = bottomSheetView.findViewById<MaterialCardView>(R.id.cardViewGallery)
        val btnDelete = bottomSheetView.findViewById<MaterialCardView>(R.id.cardViewDelete)
        viewModel.loginInfo.observe(viewLifecycleOwner) {
            btnDelete.visibility = if (it.profilePic != null) View.VISIBLE else View.GONE
        }
        btnGallery.setOnClickListener {
            pickGalleryImg()
            bottomDialog.cancel()
        }
        btnDelete.setOnClickListener {
            deleteProfilePict()
            bottomDialog.cancel()
        }
        bottomDialog.show()
    }

    private fun pickGalleryImg() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherGallery.launch(chooser)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val photoFile = uriToFile(selectedImg, requireContext())
            uploadProfilePict(photoFile)
        }
    }

    private fun uploadProfilePict(photoFile: File) {
        val imageFile = convertImageToMultipart(photoFile, "profile_picture") ?: return
        viewModel.uploadProfilePicture(imageFile)
    }

    private fun deleteProfilePict() {
        viewModel.deleteProfilePicture()
    }

    private fun setFullscreen() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.appBar.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }
}