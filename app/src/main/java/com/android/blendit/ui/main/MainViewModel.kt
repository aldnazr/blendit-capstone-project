package com.android.blendit.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.android.blendit.preference.AccountPreference
import com.android.blendit.remote.response.ItemsProduct
import com.android.blendit.viewmodel.Repository

class MainViewModel(accountPreference: AccountPreference) : ViewModel() {

    private val repository = Repository(accountPreference)

    val loadLoginInfo = repository.loginInfo

    fun getListProduct(): LiveData<PagingData<ItemsProduct>> = repository.getListProduct()

    fun getListFavorite() = repository.getListFavorite()
}