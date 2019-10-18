package com.jonathan.themoviedb.data.repository

class NetworkState(val msg: String) {

    companion object {
        val LOADED: NetworkState = NetworkState("Success")
        val LOADING: NetworkState = NetworkState("Running")
        val ERROR: NetworkState = NetworkState("Error unable to load")
        val END: NetworkState = NetworkState("The End")
    }
}