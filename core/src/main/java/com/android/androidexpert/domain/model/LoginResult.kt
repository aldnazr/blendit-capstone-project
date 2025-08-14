package com.android.androidexpert.domain.model

data class LoginResult(
    val userAccount: UserAccount,
    val error: Boolean,
    val message: String
)