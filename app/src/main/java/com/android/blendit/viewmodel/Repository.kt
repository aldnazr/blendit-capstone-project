package com.android.blendit.viewmodel

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

    init {
        loadLoginInfo()
    }

    fun loadLoginInfo() {
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

    fun uploadProfilePict(
        imageFile: MultipartBody.Part
    ): LiveData<Result<ResponseUploadProfilePicture>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadProfilePicture(
                accountPreference.getLoginInfo().token!!,
                imageFile
            )
            response.body()?.let {
                if (response.isSuccessful) {
                    emit(Result.Success(it))
                } else {
                    Result.Error(it.message)
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun deleteProfilePict(): LiveData<Result<ResponseDeleteProfilePicture>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.deleteProfilePict(
                accountPreference.getLoginInfo().token.toString()
            )
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}