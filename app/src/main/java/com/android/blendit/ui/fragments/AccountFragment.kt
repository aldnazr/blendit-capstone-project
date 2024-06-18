package com.android.blendit.ui.fragments

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.blendit.R
import com.android.blendit.databinding.FragmentAccountBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.Result
import com.android.blendit.ui.login.LoginActivity
import com.android.blendit.ui.main.MainViewModel
import com.android.blendit.utils.convertImage
import com.android.blendit.utils.uriToFile
import com.android.blendit.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val accountPreference by lazy { AccountPreference(requireActivity()) }
    private val viewModel by activityViewModels<MainViewModel> {
        ViewModelFactory.getInstance(
            accountPreference
        )
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setView() {
        viewModel.loginInfo().observe(viewLifecycleOwner) {
            binding.nameTextView.text = it.username
            binding.emailTextView.text = it.email
            if (it.profilePic != null) {
                Glide.with(requireActivity()).load(it.profilePic).into(binding.shapeableImageView)
            } else {
                binding.shapeableImageView.setImageDrawable(requireContext().getDrawable(R.drawable.ic_account_fill))
            }
        }
        binding.btnLogout.setOnClickListener {
            accountPreference.removeLoginUser()
            startActivity(
                Intent(
                    requireActivity(), LoginActivity::class.java
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
        val bottomDialog = BottomSheetDialog(requireContext())
        bottomDialog.setContentView(dialogView)
        bottomDialog.setCancelable(true)
        bottomDialog.setCanceledOnTouchOutside(true)
        val btnGallery = dialogView.findViewById<MaterialCardView>(R.id.cardViewGallery)
        val btnDelete = dialogView.findViewById<MaterialCardView>(R.id.cardViewDelete)
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
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherGallery.launch(chooser)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val photoFile = uriToFile(selectedImg, requireContext())
//            uploadProfilePict(photoFile)
        }
    }

    /*
    private fun test() {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), photoFile!!)
        val body = MultipartBody.Part.createFormData("image", photoFile?.name, requestFile)

        val call = ApiConfig.getApiService()
            .testUploadProfilePicture(accountPreference.getLoginInfo().token.toString(), body)

        call.enqueue(object : Callback<ResponseUploadProfilePicture> {
            override fun onResponse(
                p0: Call<ResponseUploadProfilePicture>,
                response: Response<ResponseUploadProfilePicture>
            ) {
                if (response.isSuccessful) {
                    accountPreference.setProfilePict(response.body()?.photoUrl)
                    viewModel.loadLoginInfo()
                } else {
                    Log.e(
                        "UploadImage",
                        "Error uploading image: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(p0: Call<ResponseUploadProfilePicture>, p1: Throwable) {
                Log.e("UploadImage", "Network error: ${p1.message}")
            }

        })
    }
    */

//    private fun uploadProfilePict(photoFile: File?) {
//        val image = convertImage(photoFile)
//        if (image != null) {
//            viewModel.uploadProfilePict(image).observe(viewLifecycleOwner) { result ->
//                when (result) {
//                    is Result.Loading -> {
//                        showLoading(true)
//                    }
//
//                    is Result.Error -> {
//                        showLoading(false)
//                        showAlert(
//                            "Gagal mengirim", "Harap coba kembali"
//                        ) { }
//                    }
//
//                    is Result.Success -> {
//                        accountPreference.setProfilePict(result.data.photoUrl)
//                        viewModel.loadLoginInfo()
//                        showLoading(false)
//                        showToast(result.data.message)
//                    }
//                }
//            }
//        }
//    }

    private fun deleteProfilePict() {
//        viewModel.deleteProfilePict().observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is Result.Loading -> {
//                    showLoading(true)
//                }
//
//                is Result.Error -> {
//                    showLoading(false)
//                    showAlert(
//                        "Gagal menghapus", "Harap coba kembali"
//                    ) { }
//                }
//
//                is Result.Success -> {
//                    accountPreference.setProfilePict(null)
//                    viewModel.loadLoginInfo()
//                    showLoading(false)
//                    showToast(result.data.message)
//                }
//            }
//        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showAlert(
        title: String, message: String, positiveAction: (dialog: DialogInterface) -> Unit
    ) {
        MaterialAlertDialogBuilder(requireActivity()).apply {
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

    private fun showToast(message: String) {
        Toast.makeText(
            requireActivity(), message, Toast.LENGTH_SHORT
        ).show()
    }
}