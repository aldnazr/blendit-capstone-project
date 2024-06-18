package com.android.blendit.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.paging.PagingData
import com.android.blendit.remote.Result
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.response.ItemsFavorite
import com.android.blendit.remote.response.ItemsProduct
import com.android.blendit.remote.response.LoginResult
import com.android.blendit.remote.response.ResponseUploadProfilePicture
import com.android.blendit.viewmodel.Repository
import okhttp3.MultipartBody

class MainViewModel(accountPreference: AccountPreference) : ViewModel() {

    private val repository = Repository(accountPreference)

    val loadLoginInfo = repository.loadLoginInfo()

    fun loginInfo() = repository.loginInfo

    fun getLoginInfo(): LiveData<LoginResult> {
        return repository.fetchLoginInfo()
    }
    val favoriteList: LiveData<List<ItemsFavorite>> = repository.favoriteList

    fun getListProduct(): LiveData<PagingData<ItemsProduct>> = repository.getListProduct()

    fun getListFavorite() = repository.getListFavorite()

    fun uploadProfilePicture(token: String, profilePicture: MultipartBody.Part): LiveData<Result<ResponseUploadProfilePicture>> = liveData {
        emit(Result.Loading)
        val result = repository.uploadProfilePicture(token, profilePicture)
        emit(result)
        // Manually update the loginInfo LiveData
        repository.loadLoginInfo()
    }

//    fun addFavorite(token: String, userId: String, productId: String) = liveData {
////        emit(Result.Loading)
//        val result = repository.addFavorite(userId, productId)
//        emit(result)
//    }


}