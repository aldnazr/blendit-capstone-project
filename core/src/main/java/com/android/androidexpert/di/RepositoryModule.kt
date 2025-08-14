package com.android.androidexpert.di

import com.android.androidexpert.data.source.repository.Repository
import com.android.androidexpert.domain.repository.IRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(repository: Repository): IRepository
}
