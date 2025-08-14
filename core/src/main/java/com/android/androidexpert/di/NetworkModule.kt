package com.android.androidexpert.di

import com.android.androidexpert.core.BuildConfig
import com.android.androidexpert.data.source.remote.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val hostname = "capstone-blendit.et.r.appspot.com"

        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=")
            .add(hostname, "sha256/YPtHaftLw6/0vnc2BnNKGF54xiCA28WFcccjkA4ypCM=")
            .add(hostname, "sha256/hxqRlPTu1bMS/0DITB1SSu0vd4u/8l8TjPgfaAp63Gc=")
            .build()

        val loggingInterceptor =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
            else HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.NONE
            )

        return OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

//    @Provides
//    fun provideApiService(): ApiService {
//        val loggingInterceptor =
//            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//        val client = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BuildConfig.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//        return retrofit.create(ApiService::class.java)
//    }
}