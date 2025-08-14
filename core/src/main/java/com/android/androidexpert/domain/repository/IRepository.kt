package com.android.androidexpert.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.androidexpert.data.ApiResponse
import com.android.androidexpert.domain.model.ChangePassResult
import com.android.androidexpert.domain.model.LoginResult
import com.android.androidexpert.domain.model.ProductFavorite
import com.android.androidexpert.domain.model.ProductInfo
import com.android.androidexpert.domain.model.RegisterResult
import com.android.androidexpert.domain.model.UserAccount
import kotlinx.coroutines.flow.Flow

interface IRepository {
    fun register(
        name: String, email: String, password: String
    ): LiveData<ApiResponse<RegisterResult>>

    fun setLoginInfo(userAccount: UserAccount)

    fun getLoginInfo(): Flow<UserAccount>

    fun removeLoginUser()

    fun login(email: String, password: String): LiveData<ApiResponse<LoginResult>>

    fun getListProduct(): Flow<ApiResponse<PagingData<ProductInfo>>>

    fun changePass(password: String): LiveData<ApiResponse<ChangePassResult>>

    fun getAllFavorites(): LiveData<List<ProductFavorite>>

    suspend fun addFavorite(productFavorite: ProductFavorite)

    suspend fun removeFavorite(productFavorite: ProductFavorite)

    suspend fun isFavorite(itemId: String): Boolean
}