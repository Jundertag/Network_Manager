package com.jayden.networkmanager.features.data.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiInfo
import android.util.Log
import com.jayden.networkmanager.features.domain.wifi.AccessPoint
import com.jayden.networkmanager.features.domain.wifi.CurrentAccessPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidDefaultNetworkDetails(appContext: Context) {

    companion object {
        private const val TAG = "AndroidDefaultNetworkDetails"
    }

    private val context = appContext.applicationContext

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    private var isRegistered = false

    private val _currentWifi = MutableStateFlow<CurrentAccessPoint?>(null)
    val currentWifi = _currentWifi.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            val transportInfo = networkCapabilities.transportInfo

            when (transportInfo) {
                is WifiInfo -> {
                    _currentWifi.value = transportInfo.toAp()
                }
                else -> {
                    _currentWifi.value = null
                }
            }
        }
    }

    fun start() {
        Log.v(TAG, "start()")
        if (isRegistered) return
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        isRegistered = true
    }

    fun stop() {
        Log.v(TAG, "stop()")
        if (!isRegistered) return
        connectivityManager.unregisterNetworkCallback(networkCallback)
        isRegistered = false
    }

    private fun WifiInfo.toAp(): CurrentAccessPoint {
        return CurrentAccessPoint(
            ssid = ssid?.removeSurrounding("\"")?.ifBlank { "<Hidden SSID>" } ?: "<Hidden SSID>",
            bssid = bssid ?: "",
            rssi = rssi
        )
    }
}