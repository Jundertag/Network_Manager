package com.jayden.wifimanager.features.details.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.jayden.wifimanager.features.details.presentation.ApDetailsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WiFiDetails(appContext: Context) {

    private val context: Context = appContext.applicationContext

    private val apDetailsViewModel = ApDetailsViewModel(this)

    private val connectivityManager: ConnectivityManager = context.getSystemService(ConnectivityManager::class.java)

    private val _activeNetwork = MutableStateFlow<Network?>(null)
    val activeNetwork: StateFlow<Network?> = _activeNetwork

    private val _capabilities = MutableStateFlow<NetworkCapabilities?>(null)
    val capabilities: StateFlow<NetworkCapabilities?> = _capabilities

    private val callback = object : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
        override fun onAvailable(network: Network) {
            _activeNetwork.value = network
            _capabilities.value = connectivityManager.getNetworkCapabilities(network)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            if (_activeNetwork.value == network) _capabilities.value = networkCapabilities
        }

        override fun onLost(network: Network) {
            if (_activeNetwork.value == network) {
                _activeNetwork.value = null
                _capabilities.value = null
            }
        }
    }

    init {
        val current = connectivityManager.activeNetwork
        _activeNetwork.value = current
        _capabilities.value = current?.let { connectivityManager.getNetworkCapabilities(it) }

        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    fun dispose() {
        connectivityManager.unregisterNetworkCallback(callback)
    }

}