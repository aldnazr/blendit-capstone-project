package com.android.androidexpert.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_items")
data class FavoriteItem(
    @PrimaryKey val id: String,
    val brand: String,
    val productName: String,
    val picture: String,
    val type: String,
    val skintone: String,
    val skinType: String,
    val undertone: String,
    val shade: String,
    val makeupType: String,
)