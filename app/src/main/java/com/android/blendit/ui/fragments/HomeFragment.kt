package com.android.blendit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.blendit.adapter.AdapterListProduct
import com.android.blendit.adapter.LoadingStateAdapter
import com.android.blendit.databinding.FragmentHomeBinding
import com.android.blendit.preference.AccountPreference
import com.android.blendit.viewmodel.ViewModelFactory
import com.android.blendit.ui.main.MainViewModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val imageList = mutableListOf<SlideModel>()
    val adapter = AdapterListProduct()
    private val accountPreference by lazy { AccountPreference(requireActivity()) }
    private val mainViewModel by activityViewModels<MainViewModel> {
        ViewModelFactory(
            accountPreference
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFullscreen()
        setView()
        setAdapter()
    }

    private fun setView() {
        if (imageList.size == 0) {
            for (i in 1..5) {
                val randomNumber = Random.nextInt(100)
                val imageRandom = "https://picsum.photos/id/$randomNumber/600/400"
                imageList.add(SlideModel(imageRandom, ScaleTypes.CENTER_CROP))
            }
        }
        binding.imageSlider.setImageList(imageList)

//        binding.tvProductRecommendation.setOnClickListener {
//            val intent = Intent(context, RecommendationActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun setFullscreen() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.appBar.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun setAdapter() {
        binding.recyclerView.adapter =
            adapter.withLoadStateFooter(LoadingStateAdapter { adapter.retry() })
        mainViewModel.getListProduct().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}