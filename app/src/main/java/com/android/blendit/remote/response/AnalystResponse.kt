package com.android.blendit.remote.response

import com.google.gson.annotations.SerializedName

data class AnalystResponse (
    @field:SerializedName("analystResult")
    val analystResult: AnalystResult,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class AnalystResult (
    @field:SerializedName("face_type")
    val face_type: String? = null,

    @field:SerializedName("skintone")
    val skintone: String? = null,

    @field:SerializedName("undertone")
    val undertone: String? = null,

    @field:SerializedName("skin_type")
    val skin_type: String? = null,

    @field:SerializedName("description")
    val description: String? = null
)