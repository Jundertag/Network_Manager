package com.jayden.networkmanager.features.data.wifi

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow

enum class WiFiState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ONLINE
}
class WiFiStats(context: Context) {

    private val connectivityManager = context.applicationContext.getSystemService(ConnectivityManager::class.java)

    private val _state = MutableStateFlow(WiFiState.DISCONNECTED)

    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
        .build()

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _state.value = WiFiState.CONNECTING
            update(connectivityManager.getNetworkCapabilities(network))
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            update(networkCapabilities)
        }

        override fun onLost(network: Network) {
            _state.value = WiFiState.DISCONNECTED
        }
    }

    private fun update(networkCapabilities: NetworkCapabilities?) {
        _state.value = when {
            networkCapabilities == null -> WiFiState.DISCONNECTED
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) -> WiFiState.ONLINE
            else -> WiFiState.CONNECTED
        }
    }

    fun start() {
        update(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork))
        connectivityManager.registerNetworkCallback(networkRequest, callback)
    }

    fun stop() {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}