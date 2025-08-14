package com.android.androidexpert.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.androidexpert.data.ApiResponse
import com.android.androidexpert.databinding.FragmentHomeBinding
import com.android.androidexpert.ui.AdapterListProduct
import com.android.androidexpert.ui.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeViewModel>()
    private lateinit var adapter: AdapterListProduct

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFullscreen()
        setAdapter()
        setView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.recyclerView.adapter = null
        _binding = null
    }

    private fun setView() {
        val favClass =
            Class.forName("com.android.favorite.presentation.favorite.presentation.favorite.FavoriteActivity")

        binding.favoriteButton.setOnClickListener {
            startActivity(Intent(requireActivity(), favClass))
        }
    }

    private fun setFullscreen() {
        binding.let {
            ViewCompat.setOnApplyWindowInsetsListener(it.root) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                binding.appBar.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
                insets
            }
        }
    }

    private fun setAdapter() {
        adapter = AdapterListProduct()
        binding.recyclerView.adapter =
            adapter.withLoadStateFooter(LoadingStateAdapter { adapter.retry() })

        viewModel.getListProduct().observe(viewLifecycleOwner) { result ->
            when (result) {
                ApiResponse.Loading -> {
                }

                is ApiResponse.Success -> {
                    adapter.submitData(lifecycle, result.data)
                }

                is ApiResponse.Error -> {
                    Toast.makeText(requireActivity(), "Load failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}