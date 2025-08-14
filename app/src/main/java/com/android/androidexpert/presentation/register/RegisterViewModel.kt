package com.android.androidexpert.presentation.register

import androidx.lifecycle.ViewModel
import com.android.androidexpert.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {
    fun userRegister(name: String, email: String, password: String) =
        useCase.register(name, email, password)
}