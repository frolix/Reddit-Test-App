package com.example.reddittestapp.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.reddittestapp.data.network.model.RedditGetTopResponse
import com.example.reddittestapp.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetTopNewsRedditVM @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private var currentResultLiveData: LiveData<PagingData<RedditGetTopResponse.DataChildren.Children>>? =
        null

    fun searchTopNewsLiveData(): LiveData<PagingData<RedditGetTopResponse.DataChildren.Children>> {
        val newResultLiveData: LiveData<PagingData<RedditGetTopResponse.DataChildren.Children>> =
            appRepository.getTopRedditPaging().cachedIn(viewModelScope)
        currentResultLiveData = newResultLiveData
        return newResultLiveData
    }
}