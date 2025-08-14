package com.android.androidexpert.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android.androidexpert.data.source.repository.Repository
import com.android.androidexpert.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {

    fun getListProduct() = useCase.getListProduct().asLiveData()
}