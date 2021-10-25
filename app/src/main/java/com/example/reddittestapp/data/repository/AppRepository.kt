package com.example.reddittestapp.data.repository

import com.example.reddittestapp.data.network.RemoteDataSource
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    suspend fun getTopReddit(after: String, limit: String) =
        remoteDataSource.getTopReddit(after, limit)

}