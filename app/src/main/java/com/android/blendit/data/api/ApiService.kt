package com.android.blendit.data.api

import android.os.Parcelable
import com.android.blendit.BuildConfig
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.Date

data class ImageAnalysisRequest(val imageUri: String)
data class ImageAnalysisResponse(val faceType: String, val skinTone: String, val description: String)

data class User(
    val login: String,
    val id: Int,
    val avatar_url: String
)

interface ApiService {
    @POST("analyzeImage")
    fun analyzeImage(
        @Body request: ImageAnalysisRequest
    ): Call<ImageAnalysisResponse>


    @GET("users/{username}/followers")
    @Headers("Authorization: ${BuildConfig.TOKEN}")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/followers")
    @Headers("Authorization: ${BuildConfig.TOKEN}")
    fun recommendation(
        @Path("username") username: String
    ): Call<ArrayList<User>>

}