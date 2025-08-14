package com.android.androidexpert.data.source.remote.network

import com.android.androidexpert.data.source.remote.response.ResponseChangePassword
import com.android.androidexpert.data.source.remote.response.ResponseFavorite
import com.android.androidexpert.data.source.remote.response.ResponseListFavorite
import com.android.androidexpert.data.source.remote.response.ResponseListProduct
import com.android.androidexpert.data.source.remote.response.ResponseLogin
import com.android.androidexpert.data.source.remote.response.ResponseRegister
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegister

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin

    @GET("listproduct")
    suspend fun listProduct(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ResponseListProduct>

    @GET("listfavorite")
    suspend fun listFavorite(
        @Header("Authorization") token: String,
        @Query("userId") userId: String
    ): Response<ResponseListFavorite>

    @FormUrlEncoded
    @POST("addfavorite")
    fun addFavorite(
        @Header("Authorization") token: String,
        @Field("userId") userId: String,
        @Field("productId") productId: String
    ): Call<ResponseFavorite>

    @DELETE("removefavorite")
    fun removeFavorite(
        @Header("Authorization") token: String,
        @Query("userId") userId: String,
        @Query("productId") productId: String
    ): Call<ResponseFavorite>

    @FormUrlEncoded
    @POST("changepassword")
    suspend fun changePass(
        @Header("Authorization") token: String,
        @Field("userId") userId: String,
        @Field("password") password: String
    ): Response<ResponseChangePassword>
}