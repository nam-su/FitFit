package com.example.fitfit.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

class NetworkManager { // 인터넷 연결상태 확인 클래스

    companion object {
        fun checkNetworkState(context: Context): Boolean {
            val connectivityManager: ConnectivityManager =
                context.getSystemService(ConnectivityManager::class.java)
            val network: Network = connectivityManager.activeNetwork ?: return false
            val actNetwork: NetworkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        }
    }

}