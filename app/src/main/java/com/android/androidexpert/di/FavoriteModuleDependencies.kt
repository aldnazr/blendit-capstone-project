package com.android.androidexpert.di

import com.android.androidexpert.domain.usecase.UseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependencies {

    fun useCase(): UseCase
}