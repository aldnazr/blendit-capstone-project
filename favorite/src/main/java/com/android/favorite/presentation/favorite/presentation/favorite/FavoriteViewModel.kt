package com.android.favorite.presentation.favorite.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.androidexpert.data.source.repository.Repository
import com.android.androidexpert.domain.model.ProductFavorite
import com.android.androidexpert.domain.usecase.UseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {

    fun listFavorite() = useCase.getAllFavorites()

    fun addFavorite(productFavorite: ProductFavorite) = viewModelScope.launch {
        useCase.addFavorite(productFavorite)
    }

    fun removeFavorite(productFavorite: ProductFavorite) = viewModelScope.launch {
        useCase.removeFavorite(productFavorite)
    }
}