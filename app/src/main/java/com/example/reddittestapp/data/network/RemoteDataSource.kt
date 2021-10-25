package com.example.reddittestapp.data.network

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RemoteDataSource @Inject constructor(
    private val apiServices: RedditApi
) : BaseDataSource() {

    suspend fun getTopReddit(after: String, limit: String) =
        getResult {
            apiServices.getTopReddit(after, limit)
        }

}