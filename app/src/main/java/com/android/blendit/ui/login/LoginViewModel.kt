package com.android.blendit.ui.login

import androidx.lifecycle.ViewModel
import com.android.blendit.preference.AccountPreference
import com.android.blendit.viewmodel.Repository

class LoginViewModel(
    loginPreferences: AccountPreference
) : ViewModel() {

    private val repository = Repository(loginPreferences)

    fun userLogin(email: String, password: String) = repository.login(email, password)
}