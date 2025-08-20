package com.jayden.networkmanager.features.data.wifi

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.util.Log
import com.jayden.networkmanager.features.domain.wifi.AccessPoint
import kotlinx.coroutines.flow.MutableStateFlow

class AndroidWifiDetailsScanner(appContext: Context) {

    companion object {
        private const val TAG = "AndroidWifiDetailsScanner"
    }

    private val context = appContext.applicationContext
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    private val _currentNetwork = MutableStateFlow<Network?>(null)

    private var networkCapabilities: NetworkCapabilities? = null

    private var isRegistered = false

    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
        override fun onAvailable(network: Network) {
            _currentNetwork.tryEmit(network)

            networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                _currentNetwork.value = network

                this@AndroidWifiDetailsScanner.networkCapabilities = networkCapabilities
            } else {
                _currentNetwork.value = null

                this@AndroidWifiDetailsScanner.networkCapabilities = null
            }
        }
    }

    fun start() {
        Log.v(TAG, "start()")
        if (isRegistered) return
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        isRegistered = true
    }

    fun stop() {
        Log.v(TAG, "stop()")
        if (!isRegistered) return
        connectivityManager.unregisterNetworkCallback(networkCallback)
        isRegistered = false
    }

    private fun NetworkCapabilities.toAp(): AccessPoint {
        val wifiInfo: WifiInfo? = transportInfo as? WifiInfo

        return if (wifiInfo != null) {
            AccessPoint(
                bssid = wifiInfo.bssid,
                ssid = wifiInfo.ssid,
                rssi = wifiInfo.rssi,
                capabilities = ""
            )
        } else {
            throw RuntimeException("WifiInfo was expected, can't convert to AccessPoint")
        }

    }
}