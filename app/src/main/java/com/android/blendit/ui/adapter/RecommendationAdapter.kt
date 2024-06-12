package com.android.blendit.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.R
import com.android.blendit.data.api.User
import com.bumptech.glide.Glide

class RecommendationAdapter(private val users: ArrayList<User>) : RecyclerView.Adapter<RecommendationAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.userName.text = user.login
        Glide.with(holder.itemView.context)
            .load(user.avatar_url)
            .into(holder.userAvatar)
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.title)
        val userAvatar: ImageView = itemView.findViewById(R.id.imageView)
    }
}