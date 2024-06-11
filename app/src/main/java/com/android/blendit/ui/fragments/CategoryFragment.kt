package com.android.blendit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.R
import com.android.blendit.data.api.ApiConfig
import com.android.blendit.data.api.User
import com.android.blendit.ui.adapter.CategoryAdapter
import com.android.blendit.ui.adapter.SpacingItemDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        recyclerView = view.findViewById(R.id.rv_category)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        // Tambahkan SpacingItemDecoration dengan jarak 16dp
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView.addItemDecoration(SpacingItemDecoration(spacingInPixels))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCategories()
    }

    private fun fetchCategories() {
        val client = ApiConfig().getApiService().getUserFollowers("mona")
        client.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    recyclerView.adapter = CategoryAdapter(users)
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                // Handle error
            }
        })
    }
}