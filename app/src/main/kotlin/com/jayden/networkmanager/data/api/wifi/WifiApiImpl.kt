package com.jayden.networkmanager.data.api.wifi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.ScanResultsCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WifiApiImpl(
    private val context: Context,
) : WifiApi {
    private var registered: Boolean = false

    private val wifiManager = context.applicationContext.getSystemService(WifiManager::class.java)

    private val _apList = MutableStateFlow<MutableList<ScanResult>>(mutableListOf())
    override val apList: StateFlow<List<ScanResult>> = _apList

    private val _restarting = MutableStateFlow(false)
    override val restarting: StateFlow<Boolean> = _restarting


    private val scanResultsCallback = object : ScanResultsCallback() {
        override fun onScanResultsAvailable() {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                _apList.tryEmit(wifiManager.scanResults)
            }
        }
    }

    private val subsystemRestartCallback = object : WifiManager.SubsystemRestartTrackingCallback() {
        override fun onSubsystemRestarted() {
            _restarting.value = false
        }

        override fun onSubsystemRestarting() {
            _restarting.value = true
        }
    }

    override fun registerCallback() {
        if (registered) return
        wifiManager.registerScanResultsCallback(context.mainExecutor, scanResultsCallback)
        wifiManager.registerSubsystemRestartTrackingCallback(context.mainExecutor, subsystemRestartCallback)
        registered = true

        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _apList.tryEmit(wifiManager.scanResults)
        }
    }

    override fun unregisterCallback() {
        if (!registered) return
        wifiManager.unregisterScanResultsCallback(scanResultsCallback)
        wifiManager.unregisterSubsystemRestartTrackingCallback(subsystemRestartCallback)
        registered = false
    }
}