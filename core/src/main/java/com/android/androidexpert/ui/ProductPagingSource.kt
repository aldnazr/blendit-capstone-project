package com.android.androidexpert.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.androidexpert.data.source.local.AccountPreference
import com.android.androidexpert.data.source.remote.network.ApiService
import com.android.androidexpert.domain.model.ProductInfo
import com.android.androidexpert.utils.DataMapper

class ProductPagingSource(
    private val pref: AccountPreference, private val apiService: ApiService
) : PagingSource<Int, ProductInfo>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductInfo> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = pref.getLoginInfo().token.toString()
            if (token.isEmpty()) {
                return LoadResult.Error(Exception("Token is empty"))
            }

            val response = apiService.listProduct(token, position, params.loadSize)
            if (!response.isSuccessful) {
                return LoadResult.Error(Exception("Failed to load"))
            }

            val items = response.body()?.items ?: emptyList()
            val productInfoList = DataMapper.toDomainProduct(items)

            LoadResult.Page(
                data = productInfoList,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (items.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
