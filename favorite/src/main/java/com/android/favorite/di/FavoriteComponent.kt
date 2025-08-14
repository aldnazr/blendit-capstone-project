package com.android.favorite.di

import android.content.Context
import com.android.androidexpert.di.AppModule
import com.android.androidexpert.di.FavoriteModuleDependencies
import com.android.favorite.presentation.favorite.presentation.favorite.FavoriteActivity
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [FavoriteModuleDependencies::class])
interface FavoriteComponent {
    fun inject(activity: FavoriteActivity)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(favoriteModuleDependencies: FavoriteModuleDependencies): Builder
        fun build(): FavoriteComponent
    }
}