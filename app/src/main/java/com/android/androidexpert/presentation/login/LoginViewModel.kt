package com.android.androidexpert.presentation.login

import androidx.lifecycle.ViewModel
import com.android.androidexpert.data.source.repository.Repository
import com.android.androidexpert.domain.model.UserAccount
import com.android.androidexpert.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {
    fun getLoginInfo(email: String, password: String) = useCase.login(email, password)
    fun setLoginInfo(userAccount: UserAccount) = useCase.setLoginInfo(userAccount)
}