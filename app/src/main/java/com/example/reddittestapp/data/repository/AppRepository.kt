package com.example.reddittestapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.reddittestapp.data.network.RemoteDataSource
import com.example.reddittestapp.data.network.model.RedditGetTopResponse
import com.example.reddittestapp.paging.TopNewsPagingSource
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val topNewsPagingSource: TopNewsPagingSource

) {

    fun getTopRedditPaging(): LiveData<PagingData<RedditGetTopResponse.DataChildren.Children>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = 10, initialLoadSize = 20),
            pagingSourceFactory = {
                topNewsPagingSource
            }
        ).liveData
    }


}