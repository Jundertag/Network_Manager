package com.jayden.networkmanager.features.data.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AndroidNetworkDetails(appContext: Context) {

    enum class NetworkType {
        WIFI,
        CELLULAR,
        ETHERNET,
        BLUETOOTH,
        VPN,
        UNKNOWN
    }

    private val context = appContext.applicationContext

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    private val _networks = MutableStateFlow<Map<Network, NetworkType>>(emptyMap())
    val networks = _networks.asStateFlow()

    private var isRegistered = false

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _networks.update { it + (network to NetworkType.UNKNOWN) }
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            val type = networkCapabilities.toNetworkType()

            if (_networks.value[network] != type) {
                _networks.update { it + (network to type) }
            }
        }

        override fun onLost(network: Network) {
            _networks.update { it - network }
        }
    }

    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
        .build()

    fun start() {
        if (isRegistered) return
        connectivityManager.registerNetworkCallback(
            networkRequest,
            networkCallback
        )
        isRegistered = true
    }

    fun stop() {
        if (!isRegistered) return
        connectivityManager.unregisterNetworkCallback(networkCallback)
        isRegistered = false
    }

    private fun NetworkCapabilities.toNetworkType(): NetworkType = when {
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
        hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> NetworkType.BLUETOOTH
        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> NetworkType.VPN
        else -> NetworkType.UNKNOWN
    }
}