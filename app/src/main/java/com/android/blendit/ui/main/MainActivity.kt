package com.android.blendit.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.android.blendit.R
import com.android.blendit.databinding.ActivityMainBinding
import com.android.blendit.ui.CameraActivity
import com.android.blendit.ui.analysis.AnalysisActivity
import com.android.blendit.ui.fragments.AccountFragment
import com.android.blendit.ui.fragments.CategoryFragment
import com.android.blendit.ui.fragments.FavoriteFragment
import com.android.blendit.ui.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openCameraActivity()
        } else {
            Toast.makeText(
                this,
                "Camera permission is required to use this feature",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFullscreen()
        switchFragment(HomeFragment())
        setView()
        setupOnBackPressedCallback()
        setupFabClick()
    }

    private fun setView() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> switchFragment(HomeFragment())
                R.id.wishlist -> switchFragment(FavoriteFragment())
                R.id.category -> switchFragment(CategoryFragment())
                R.id.account -> switchFragment(AccountFragment())
            }
            true
        }

        binding.fabScan.setOnClickListener {
            Intent(this, AnalysisActivity::class.java)
        }
    }

    private fun setupOnBackPressedCallback() {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                if (currentFragment !is HomeFragment) {
                    binding.bottomNavigation.selectedItemId = R.id.home
                } else {
                    finishAffinity()
                }
            }

        })
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
        }
    }

    private fun setFullscreen() {
//        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
//            binding.bottomAppBar.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }
    }

    private fun setupFabClick() {
        binding.fabScan.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCameraActivity()
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCameraActivity() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }
}