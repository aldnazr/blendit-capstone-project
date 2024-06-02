package com.android.blendit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.response.ResponseLogin
import com.android.blendit.remote.response.ResponseRegister
import com.android.blendit.remote.retrofit.ApiConfig
import com.android.blendit.remote.retrofit.ApiService
import com.android.blendit.remote.Result

class Repository(private val accountPreference: AccountPreference) {

    private val apiService: ApiService = ApiConfig.getApiService()

    fun register(
        name: String, email: String, password: String
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

    fun login(
        email: String, password: String
    ): LiveData<Result<ResponseLogin>> = liveData {
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
}