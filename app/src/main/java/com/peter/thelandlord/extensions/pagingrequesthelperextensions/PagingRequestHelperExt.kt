package com.peter.thelandlord.extensions.pagingrequesthelperextensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import com.peter.thelandlord.data.networkstate.NetworkState

private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String{
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}

fun PagingRequestHelper.createNetworkStatusLiveData(): LiveData<NetworkState>{
    val networkState = MutableLiveData<NetworkState>()
    addListener{
        report ->
        when {
            report.hasError() -> networkState.postValue(NetworkState.error(getErrorMessage(report)))
            report.hasRunning() -> networkState.postValue(NetworkState.LOADING)
            else -> networkState.postValue(NetworkState.LOADED)
        }
    }
    return networkState
}
