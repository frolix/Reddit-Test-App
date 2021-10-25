package com.example.reddittestapp.data.network

import com.example.reddittestapp.data.network.model.RedditGetTopResponse
import com.example.reddittestapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {

    @GET(Constants.GET_TOP)
    fun getTopReddit(
        @Query("after") after: String,
        @Query("limit") limit: String
    ): Response<RedditGetTopResponse>

}