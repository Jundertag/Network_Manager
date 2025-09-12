package com.jayden.networkmanager.data.api.wifi

import android.net.wifi.ScanResult
import kotlinx.coroutines.flow.StateFlow

interface WifiApi {
    val apList: StateFlow<List<ScanResult>>
    val restarting: StateFlow<Boolean>
    fun registerCallback()
    fun unregisterCallback()
}