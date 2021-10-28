package com.example.reddittestapp.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.reddittestapp.data.network.model.RedditGetTopResponse
import com.example.reddittestapp.paging.TopNewsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GetTopNewsRedditVM @Inject constructor(
//    private val appRepository: AppRepository,
    private val topNewsPagingSource: TopNewsPagingSource
) : ViewModel() {

    //this code using flow
    val searchTopNewsLiveData: Flow<PagingData<RedditGetTopResponse.DataChildren.Children>> =
        Pager(config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { topNewsPagingSource }
        ).flow.cachedIn(viewModelScope)


    //this code using with livedata
    /*
    private var currentResultLiveData: LiveData<PagingData<RedditGetTopResponse.DataChildren.Children>>? =
        null

    fun searchTopNewsLiveData(): LiveData<PagingData<RedditGetTopResponse.DataChildren.Children>> {
        return if(currentResultLiveData != null) currentResultLiveData as LiveData<PagingData<RedditGetTopResponse.DataChildren.Children>>
        else {
            val newResultLiveData: LiveData<PagingData<RedditGetTopResponse.DataChildren.Children>> =
                appRepository.getTopRedditPaging().cachedIn(viewModelScope)
            currentResultLiveData = newResultLiveData
            Log.d("GetTopNewsRedditVM", "searchTopNewsLiveData: ")

            newResultLiveData
        }
    }

     */
}

