package com.android.blendit.remote.response

import com.google.gson.annotations.SerializedName

data class FavoriteResponse (
    @field:SerializedName("error")
    val status: String,

    @field:SerializedName("message")
    val message: String
)