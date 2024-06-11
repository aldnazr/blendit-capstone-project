package com.android.blendit.ui.recommendation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.blendit.R
import com.android.blendit.data.api.ApiConfig
import com.android.blendit.data.api.User
import com.android.blendit.databinding.ActivityRecommendationBinding
import com.android.blendit.ui.adapter.RecommendationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fetchRecommendations()
    }

    private fun fetchRecommendations() {
        val client = ApiConfig().getApiService().recommendation("mona")
        client.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let {
                        setupRecyclerView(it)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun setupRecyclerView(users: ArrayList<User>) {
        binding.rvRecommendation.layoutManager = LinearLayoutManager(this)
        val adapter = RecommendationAdapter(users)
        binding.rvRecommendation.adapter = adapter
    }
}