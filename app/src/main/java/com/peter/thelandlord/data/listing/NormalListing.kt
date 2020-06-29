package com.peter.thelandlord.data.listing

import androidx.lifecycle.LiveData
import com.peter.thelandlord.data.networkstate.NetworkState

data class NormalListing<T> (
    var liveList: LiveData<List<T>>,
    var refreshState: LiveData<NetworkState>,
    var refresh: () -> Unit,
    var retry: () -> Unit
)