package com.android.blendit.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class ImageAnalysisRequest(val imageUri: String)
data class ImageAnalysisResponse(val faceType: String, val skinTone: String, val description: String)

interface ApiService {
    @POST("analyzeImage")
    fun analyzeImage(
        @Body request: ImageAnalysisRequest
    ): Call<ImageAnalysisResponse>
}