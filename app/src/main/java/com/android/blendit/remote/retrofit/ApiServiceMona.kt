package com.android.blendit.remote.retrofit

import com.android.blendit.data.ImageAnalysisRequest
import com.android.blendit.data.ImageAnalysisResponse
import com.android.blendit.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceMona {
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