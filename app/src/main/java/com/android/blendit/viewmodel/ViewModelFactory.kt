package com.android.blendit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.blendit.preference.AccountPreference
import com.android.blendit.ui.login.LoginViewModel
import com.android.blendit.ui.main.MainViewModel
import com.android.blendit.ui.register.RegisterViewModel

class ViewModelFactory(
    private val accountPreference: AccountPreference
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                accountPreference
            ) as T

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(
                accountPreference
            ) as T

            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                accountPreference
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            accountPreference: AccountPreference
        ): ViewModelFactory = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ViewModelFactory(accountPreference)
        }
    }
}