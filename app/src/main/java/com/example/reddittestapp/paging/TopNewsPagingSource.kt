package com.example.reddittestapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.reddittestapp.data.network.RedditApi
import com.example.reddittestapp.data.network.model.RedditGetTopResponse
import retrofit2.HttpException
import javax.inject.Inject

class TopNewsPagingSource @Inject constructor(
    private val apiService: RedditApi
) : PagingSource<String, RedditGetTopResponse.DataChildren.Children>() {

    override fun getRefreshKey(state: PagingState<String, RedditGetTopResponse.DataChildren.Children>): String {
        return state.anchorPosition.toString()
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditGetTopResponse.DataChildren.Children> {
        try {
            val page: String = params.key .toString()
            val pageSize: String = params.loadSize.toString()
            val response = apiService.getTopReddit(page, pageSize )

            Log.d("TopNewsPagingSource", "HttpException: ${response.body()}")

            return if (response.isSuccessful) {
                val topNewsReddit = checkNotNull(response.body())
                val topNewsRedditList = checkNotNull(response.body()?.data?.children)
                val prevKey = topNewsReddit.data?.before
                val nextKey = topNewsReddit.data?.after
                LoadResult.Page(topNewsRedditList, prevKey, nextKey)
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            Log.d("TopNewsPagingSource", "HttpException: $e")
            return LoadResult.Error(e)
        } catch (e: Exception) {
            Log.d("TopNewsPagingSource", "Exception: $e")
            return LoadResult.Error(e)
        }
    }

}