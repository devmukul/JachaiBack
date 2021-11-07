package com.jachai.jachaimart.utils

data class NetworkState private constructor(
    val status: Status,
    val t: Throwable? = null
) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val REFRESHING = NetworkState(Status.REFRESH)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(t: Throwable) = NetworkState(Status.FAILED, t)
    }
}

