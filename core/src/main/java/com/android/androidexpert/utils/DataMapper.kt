package com.android.androidexpert.utils

import com.android.androidexpert.data.source.local.entity.FavoriteItem
import com.android.androidexpert.data.source.remote.response.ItemsProduct
import com.android.androidexpert.data.source.remote.response.ResponseChangePassword
import com.android.androidexpert.data.source.remote.response.ResponseLogin
import com.android.androidexpert.data.source.remote.response.ResponseRegister
import com.android.androidexpert.domain.model.ChangePassResult
import com.android.androidexpert.domain.model.LoginResult
import com.android.androidexpert.domain.model.ProductFavorite
import com.android.androidexpert.domain.model.ProductInfo
import com.android.androidexpert.domain.model.RegisterResult
import com.android.androidexpert.domain.model.UserAccount

object DataMapper {
    fun toDomainRegister(response: ResponseRegister): RegisterResult {
        return RegisterResult(
            error = response.error, message = response.message
        )
    }

    fun toDomainLogin(response: ResponseLogin): LoginResult {
        return LoginResult(
            userAccount = UserAccount(
                response.loginResult.userId,
                response.loginResult.email,
                response.loginResult.username,
                response.loginResult.token,
                response.loginResult.profilePic
            ), error = response.error, message = response.message
        )
    }

    fun toDomainFavorite(itemsFavorite: List<FavoriteItem>): List<ProductFavorite> {
        return itemsFavorite.map { favoriteItem ->
            ProductFavorite(
                productId = favoriteItem.id,
                undertone = favoriteItem.undertone,
                shade = favoriteItem.shade,
                type = favoriteItem.type,
                skintone = favoriteItem.skintone,
                skinType = favoriteItem.skinType,
                brand = favoriteItem.brand,
                productName = favoriteItem.productName,
                makeupType = favoriteItem.makeupType,
                picture = favoriteItem.picture
            )
        }
    }

    fun toDomainProduct(itemsProduct: List<ItemsProduct>): List<ProductInfo> {
        return itemsProduct.map { item ->
            ProductInfo(
                item.id,
                item.brand,
                item.productName,
                item.picture,
                item.type,
                item.skintone,
                item.skinType,
                item.undertone,
                item.shade,
                item.makeupType
            )
        }
    }

    fun toDomainChangePass(response: ResponseChangePassword): ChangePassResult {
        return ChangePassResult(
            error = response.error, message = response.message
        )
    }

    fun toDomainFavoriteItem(productFavorite: ProductFavorite): FavoriteItem {
        return FavoriteItem(
            productFavorite.productId,
            productFavorite.brand,
            productFavorite.productName,
            productFavorite.picture,
            productFavorite.type,
            productFavorite.skintone,
            productFavorite.skinType,
            productFavorite.undertone,
            productFavorite.shade,
            productFavorite.makeupType
        )
    }
}