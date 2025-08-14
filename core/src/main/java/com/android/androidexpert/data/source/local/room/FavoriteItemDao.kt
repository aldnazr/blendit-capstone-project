package com.android.androidexpert.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.androidexpert.data.source.local.entity.FavoriteItem

@Dao
interface FavoriteItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteItem: FavoriteItem)

    @Query("SELECT * FROM favorite_items")
    suspend fun getAllFavoriteItems(): List<FavoriteItem>

    @Delete
    suspend fun delete(favoriteItem: FavoriteItem)

    @Query("SELECT * FROM favorite_items WHERE id = :id LIMIT 1")
    suspend fun findByItemId(id: String): FavoriteItem?
}