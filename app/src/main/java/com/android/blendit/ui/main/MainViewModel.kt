package com.android.blendit.ui.main

import androidx.lifecycle.ViewModel
import com.android.blendit.preference.AccountPreference
import com.android.blendit.viewmodel.Repository

class MainViewModel(accountPreference: AccountPreference) : ViewModel() {

    private val repository = Repository(accountPreference)

    val loadLoginInfo = repository.loginInfo
}