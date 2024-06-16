package com.android.blendit.data

data class ImageAnalysisRequest(
    val imageUri: String
)

data class ImageAnalysisResponse(
    val faceType: String,
    val skinTone: String,
    val description: String
)

data class User(
    val login: String,
    val id: Int,
    val avatarUrl: String
)