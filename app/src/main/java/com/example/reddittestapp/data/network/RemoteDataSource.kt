package com.example.reddittestapp.data.network

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiServices: RedditApi
) : BaseDataSource() {

    suspend fun getTopReddit(after: String, limit: String) =
        getResult {
            apiServices.getTopReddit(after, limit)
        }

}