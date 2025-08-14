package com.android.androidexpert.di

import com.android.androidexpert.domain.usecase.Interactor
import com.android.androidexpert.domain.usecase.UseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /*@Provides
    @Singleton
    fun provideRepository(
        accountPreference: AccountPreference,
        favoriteItemDao: FavoriteItemDao
    ): Repository {
        return Repository(accountPreference, favoriteItemDao)
    }*/

    @Binds
    @Singleton
    abstract fun provideUseCase(interactor: Interactor): UseCase
}


