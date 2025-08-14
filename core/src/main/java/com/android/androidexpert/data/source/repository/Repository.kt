package com.android.androidexpert.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.androidexpert.data.ApiResponse
import com.android.androidexpert.data.source.local.AccountPreference
import com.android.androidexpert.data.source.local.room.FavoriteItemDao
import com.android.androidexpert.data.source.remote.network.ApiConfig
import com.android.androidexpert.data.source.remote.network.ApiService
import com.android.androidexpert.domain.model.ChangePassResult
import com.android.androidexpert.domain.model.LoginResult
import com.android.androidexpert.domain.model.ProductFavorite
import com.android.androidexpert.domain.model.ProductInfo
import com.android.androidexpert.domain.model.RegisterResult
import com.android.androidexpert.domain.model.UserAccount
import com.android.androidexpert.domain.repository.IRepository
import com.android.androidexpert.ui.ProductPagingSource
import com.android.androidexpert.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val accountPreference: AccountPreference,
    private val favoriteItemDao: FavoriteItemDao,
    private val apiService: ApiService
) : IRepository {

    private val _loginInfo = MutableLiveData<UserAccount>()
    private val loginInfo: LiveData<UserAccount> = _loginInfo

    init {
        loadLoginInfo()
    }

    override fun setLoginInfo(userAccount: UserAccount) {
        accountPreference.setLoginInfo(userAccount)
    }

    override fun getLoginInfo(): Flow<UserAccount> {
        return loginInfo.asFlow()
    }

    override fun removeLoginUser() {
        accountPreference.removeLoginUser()
    }

    private fun loadLoginInfo() {
        val loginResult = accountPreference.getLoginInfo()
        _loginInfo.value = loginResult
    }

    override fun register(
        name: String, email: String, password: String
    ): LiveData<ApiResponse<RegisterResult>> = liveData {
        emit(ApiResponse.Loading)
        try {
            // Fetch the response from the API
            val response = apiService.register(name, email, password)

            // Map the response to the domain model
            val domainModel = DataMapper.toDomainRegister(response)

            // Emit the result based on the success or failure of the response
            if (response.error) {
                emit(ApiResponse.Error(domainModel.message))
            } else {
                emit(ApiResponse.Success(domainModel))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }

    override fun login(email: String, password: String): LiveData<ApiResponse<LoginResult>> =
        liveData {
            emit(ApiResponse.Loading)
            try {
                // Fetch the response from the API
                val response = apiService.login(email, password)

                // Map the response to the domain model
                val domainModel = DataMapper.toDomainLogin(response)

                // Emit the result based on the success or failure of the response
                if (domainModel.error) {
                    emit(ApiResponse.Error(domainModel.message))
                } else {
                    emit(ApiResponse.Success(domainModel))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }

    override fun getListProduct(): Flow<ApiResponse<PagingData<ProductInfo>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val pagingDataFlow = Pager(PagingConfig(20),
                pagingSourceFactory = {
                    ProductPagingSource(
                        accountPreference,
                        apiService
                    )
                }).flow.flowOn(Dispatchers.Main)

            pagingDataFlow
                .collect { pagingData ->
                    emit(ApiResponse.Success(pagingData))
                }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override fun changePass(password: String): LiveData<ApiResponse<ChangePassResult>> = liveData {
        emit(ApiResponse.Loading)
        try {
            val response = apiService.changePass(
                accountPreference.getLoginInfo().token.toString(),
                accountPreference.getLoginInfo().userId.toString(),
                password
            )
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    // Map the response to the domain model
                    val domainModel = DataMapper.toDomainChangePass(body)
                    emit(ApiResponse.Success(domainModel))
                } ?: emit(ApiResponse.Error("Empty response body"))
            } else {
                emit(ApiResponse.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message ?: "Unknown error"))
        }
    }

    override fun getAllFavorites(): LiveData<List<ProductFavorite>> = liveData {
        val favoriteItems = favoriteItemDao.getAllFavoriteItems()
        val productFavorites = DataMapper.toDomainFavorite(favoriteItems)
        emit(productFavorites)
    }

    override suspend fun addFavorite(productFavorite: ProductFavorite) {
        favoriteItemDao.insert(DataMapper.toDomainFavoriteItem(productFavorite))
    }

    override suspend fun removeFavorite(productFavorite: ProductFavorite) {
        favoriteItemDao.delete(DataMapper.toDomainFavoriteItem(productFavorite))
    }

    override suspend fun isFavorite(itemId: String) = favoriteItemDao.findByItemId(itemId) != null
}