package com.android.androidexpert.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.android.androidexpert.R
import com.android.androidexpert.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setFullscreen()
        setupNavHost()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    private fun setupNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val nav = binding?.bottomNavigation
        NavigationUI.setupWithNavController(nav!!, navController)
    }

    private fun setFullscreen() {
        binding?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it.root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, 0, systemBars.right, 0)
                insets
            }
        }
    }
}