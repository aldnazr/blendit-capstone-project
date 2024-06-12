package com.android.blendit.data.api

import com.android.blendit.BuildConfig
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

data class ImageAnalysisRequest(val imageUri: String)
data class ImageAnalysisResponse(val faceType: String, val skinTone: String, val description: String)

data class User(
    val login: String,
    val id: Int,
    val avatarUrl: String
)

interface ApiService {
    @POST("analyzeImage")
    fun analyzeImage(
        @Body request: ImageAnalysisRequest
    ): Call<ImageAnalysisResponse>

    @GET("users/{username}/followers")
//    @Headers("Authorization: ${BuildConfig.TOKEN}")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/followers")
//    @Headers("Authorization: ${BuildConfig.TOKEN}")
    fun recommendation(
        @Path("username") username: String
    ): Call<ArrayList<User>>

}