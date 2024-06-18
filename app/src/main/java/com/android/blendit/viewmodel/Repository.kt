package com.android.blendit.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.android.blendit.data.ProductPagingSource
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.Result
import com.android.blendit.remote.response.CategoryResponse
import com.android.blendit.remote.response.CategoryTutorialResponse
import com.android.blendit.remote.response.FavoriteResponse
import com.android.blendit.remote.response.ItemsFavorite
import com.android.blendit.remote.response.ItemsProduct
import com.android.blendit.remote.response.LoginResult
import com.android.blendit.remote.response.ResponseDeleteProfilePicture
import com.android.blendit.remote.response.ResponseLogin
import com.android.blendit.remote.response.ResponseRegister
import com.android.blendit.remote.response.ResponseUploadProfilePicture
import com.android.blendit.remote.retrofit.ApiConfig
import com.android.blendit.remote.retrofit.ApiService
import okhttp3.MultipartBody

class Repository(private val accountPreference: AccountPreference) {

    private val apiService: ApiService = ApiConfig.getApiService()
    private val _loginInfo = MutableLiveData<LoginResult>()
    val loginInfo: LiveData<LoginResult> = _loginInfo

    private val _favoriteList = MutableLiveData<List<ItemsFavorite>>()
    val favoriteList: LiveData<List<ItemsFavorite>> = _favoriteList

    init {
        loadLoginInfo()
    }

    private fun loadLoginInfo() {
        val loginResult = accountPreference.getLoginInfo()
        _loginInfo.value = loginResult
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<ResponseRegister>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(
                name, email, password
            )
            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<ResponseLogin>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(
                email, password
            )
            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
                // Logging token here
                Log.d("Repository", "Login success. Token: ${response.loginResult.token}")
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListProduct(): LiveData<PagingData<ItemsProduct>> {
        return Pager(
            PagingConfig(5), null
        ) { ProductPagingSource(accountPreference, apiService) }.liveData
    }

    fun getListFavorite(): LiveData<Result<List<ItemsFavorite>>> =
        liveData {
            emit(Result.Loading)
            try {
                val response =
                    apiService.listFavorite(
                        accountPreference.getLoginInfo().token.toString(),
                        accountPreference.getLoginInfo().userId.toString()
                    )
                if (response.isSuccessful) {
                    response.body()?.let { Result.Success(it.items) }?.let { emit(it) }
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }

    suspend fun addFavorite(productId: String): Result<FavoriteResponse> {
        return try {
            val response = apiService.addFavorite(
                accountPreference.getLoginInfo().token.toString(),
                accountPreference.getLoginInfo().userId.toString(),
                productId
            )
            if (response.error) {
                Result.Error(response.message)
            } else {
                // Re-fetch favorite list
                getListFavorite()
                Result.Success(response)
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error adding favorite: ${e.message}", e)
            Result.Error(e.message ?: "Unknown error")
        }
    }

    fun getCategory(token: String): LiveData<Result<CategoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getCategory(token)
            if (response.status == "error") {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getCategoryTutorial(
        token: String,
        categoryId: String
    ): LiveData<Result<CategoryTutorialResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getCategoryTutorial(token, categoryId)
            if (response.status == "error") {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadProfilePicture(
        profilePicture: MultipartBody.Part
    ): LiveData<Result<ResponseUploadProfilePicture>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadProfilePicture(
                accountPreference.getLoginInfo().token.toString(),
                profilePicture
            )
            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                accountPreference.setProfilePict(response.photoUrl)
                loadLoginInfo()
                Result.Success(response)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    fun deleteProfilePicture(
    ): LiveData<Result<ResponseDeleteProfilePicture>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.deleteProfilePict(
                accountPreference.getLoginInfo().token.toString()
            )
            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                accountPreference.setProfilePict(null)
                Result.Success(response)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}