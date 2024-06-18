package com.android.blendit.ui.main

import androidx.lifecycle.ViewModel
import com.android.blendit.preference.AccountPreference
import com.android.blendit.viewmodel.Repository
import okhttp3.MultipartBody

class MainViewModel(accountPreference: AccountPreference) : ViewModel() {

    private val repository = Repository(accountPreference)

    fun loginInfo() = repository.loginInfo

    val favoriteList = repository.favoriteList

    fun getListProduct() = repository.getListProduct()

    fun getListFavorite() = repository.getListFavorite()

    fun uploadProfilePicture(profilePicture: MultipartBody.Part) =
        repository.uploadProfilePicture(profilePicture)

    fun deleteProfilePicture() = repository.deleteProfilePicture()

//    fun addFavorite(token: String, userId: String, productId: String) = liveData {
////        emit(Result.Loading)
//        val result = repository.addFavorite(userId, productId)
//        emit(result)
//    }


}