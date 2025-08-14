package com.android.androidexpert.di

import android.content.Context
import androidx.room.Room
import com.android.androidexpert.data.source.local.room.AppDatabase
import com.android.androidexpert.data.source.local.room.FavoriteItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("android".toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    fun provideFavoriteItemDao(appDatabase: AppDatabase): FavoriteItemDao {
        return appDatabase.favoriteItemDao()
    }
}