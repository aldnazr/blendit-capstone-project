package com.android.blendit.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.blendit.R

import com.bumptech.glide.Glide

//class CategoryAdapter(private val users: List<User>) : RecyclerView.Adapter<CategoryAdapter.UserViewHolder>() {
//
//    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val name: TextView = view.findViewById(R.id.tv_category)
//        val photo: ImageView = view.findViewById(R.id.iv_category)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
//        return UserViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val user = users[position]
//        holder.name.text = user.login
//        Glide.with(holder.itemView.context)
//            .load(user.avatarUrl)
//            .into(holder.photo)
//    }
//
//    override fun getItemCount(): Int = users.size
//}