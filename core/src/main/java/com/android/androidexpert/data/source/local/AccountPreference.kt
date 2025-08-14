package com.android.androidexpert.data.source.local

import android.content.Context
import com.android.androidexpert.domain.model.UserAccount
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountPreference @Inject constructor(
    @ApplicationContext context: Context
) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "login_pref"
        const val USER_ID = "userId"
        const val EMAIL = "email"
        const val USERNAME = "username"
        const val TOKEN = "token"
        const val PROFILEPIC = "profilepic"
    }

    fun setLoginInfo(userAccount: UserAccount) {
        val editor = preferences.edit()
        editor.putString(USER_ID, userAccount.userId)
        editor.putString(USERNAME, userAccount.username)
        editor.putString(TOKEN, userAccount.token)
        editor.putString(EMAIL, userAccount.email)
        editor.putString(PROFILEPIC, userAccount.profilePic)
        editor.apply()
    }

    fun getLoginInfo(): UserAccount {
        val userId = preferences.getString(USER_ID, null)
        val userName = preferences.getString(USERNAME, null)
        val token = preferences.getString(TOKEN, null)
        val email = preferences.getString(EMAIL, null)
        val profilePic = preferences.getString(PROFILEPIC, null)
        return UserAccount(userId, email, userName, token, profilePic)
    }

    fun removeLoginUser() {
        val editor = preferences.edit().clear()
        editor.apply()
    }
}