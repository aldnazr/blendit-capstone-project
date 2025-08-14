package com.android.androidexpert.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.androidexpert.data.source.repository.Repository
import com.android.androidexpert.domain.model.ProductFavorite
import com.android.androidexpert.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {

    suspend fun isFavorite(itemId: String): Boolean {
        return useCase.isFavorite(itemId)
    }

    fun addFavorite(productFavorite: ProductFavorite) = viewModelScope.launch {
        useCase.addFavorite(productFavorite)
    }

    fun removeFavorite(productFavorite: ProductFavorite) = viewModelScope.launch {
        useCase.removeFavorite(productFavorite)
    }
}