package com.android.blendit.ui.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.android.blendit.R
import com.android.blendit.databinding.ActivityMainBinding
import com.android.blendit.ui.fragments.AccountFragment
import com.android.blendit.ui.fragments.CategoryFragment
import com.android.blendit.ui.fragments.HistoryFragment
import com.android.blendit.ui.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFullscreen()
        switchFragment(HomeFragment())
        setupBottomNavigation()
        setupOnBackPressedCallback()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> switchFragment(HomeFragment())
                R.id.wishlist -> switchFragment(HistoryFragment())
                R.id.category -> switchFragment(CategoryFragment())
                R.id.account -> switchFragment(AccountFragment())
            }
            true
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
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
//            binding.bottomAppBar.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }
    }
}