package com.android.blendit.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.android.blendit.databinding.FragmentHomeBinding
import com.android.blendit.ui.recommendation.RecommendationActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val imageList = mutableListOf<SlideModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFullscreen()
        setView()
    }

    private fun setView() {
        for (i in 1..5) {
            val randomNumber = Random.nextInt(100)
            val imageRandom = "https://picsum.photos/id/$randomNumber/600/400"
            imageList.add(SlideModel(imageRandom,ScaleTypes.CENTER_CROP))
        }
        binding.imageSlider.setImageList(imageList)

        binding.tvProductRecommendation.setOnClickListener {
            val intent = Intent(context, RecommendationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setFullscreen() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.appBar.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

}