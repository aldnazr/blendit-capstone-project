package com.android.favorite.presentation.favorite.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.androidexpert.data.source.repository.Repository
import com.android.androidexpert.domain.usecase.UseCase
import com.android.favorite.presentation.favorite.presentation.favorite.FavoriteViewModel
import javax.inject.Inject

class FavoriteViewModelFactory @Inject constructor(private val useCase: UseCase) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> FavoriteViewModel(
                useCase
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}