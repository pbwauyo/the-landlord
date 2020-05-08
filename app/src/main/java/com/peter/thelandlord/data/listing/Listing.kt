package com.peter.thelandlord.data.listing

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.peter.thelandlord.data.networkstate.NetworkState

data class Listing<T> (
    val livePagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val refreshState: LiveData<NetworkState>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)