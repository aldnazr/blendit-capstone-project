package com.android.androidexpert.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.androidexpert.data.ApiResponse
import com.android.androidexpert.domain.model.ChangePassResult
import com.android.androidexpert.domain.model.LoginResult
import com.android.androidexpert.domain.model.ProductFavorite
import com.android.androidexpert.domain.model.ProductInfo
import com.android.androidexpert.domain.model.RegisterResult
import com.android.androidexpert.domain.model.UserAccount
import com.android.androidexpert.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Interactor @Inject constructor(
    private val iRepository: IRepository
) : UseCase {

    override fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ApiResponse<RegisterResult>> {
        return iRepository.register(name, email, password)
    }

    override fun setLoginInfo(userAccount: UserAccount) {
        iRepository.setLoginInfo(userAccount)
    }

    override fun getLoginInfo(): Flow<UserAccount> {
        return iRepository.getLoginInfo()
    }

    override fun removeLoginUser() {
        iRepository.removeLoginUser()
    }

    override fun login(email: String, password: String): LiveData<ApiResponse<LoginResult>> {
        return iRepository.login(email, password)
    }

    override fun getListProduct(): Flow<ApiResponse<PagingData<ProductInfo>>> {
        return iRepository.getListProduct()
    }

    override fun changePass(password: String): LiveData<ApiResponse<ChangePassResult>> {
        return iRepository.changePass(password)
    }

    override suspend fun removeFavorite(productFavorite: ProductFavorite) {
        iRepository.removeFavorite(productFavorite)
    }

    override fun getAllFavorites(): LiveData<List<ProductFavorite>> {
        return iRepository.getAllFavorites()
    }

    override suspend fun addFavorite(productFavorite: ProductFavorite) {
        iRepository.addFavorite(productFavorite)
    }

    override suspend fun isFavorite(itemId: String): Boolean {
        return iRepository.isFavorite(itemId)
    }
}
