package com.android.androidexpert.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android.androidexpert.data.source.repository.Repository
import com.android.androidexpert.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {

    fun removeLoginUser() = useCase.removeLoginUser()

    fun loginInfo() = useCase.getLoginInfo().asLiveData()

    fun changePass(password: String) = useCase.changePass(password)
}