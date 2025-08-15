package com.jayden.networkmanager.features.scan.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class WiFiScanner(appContext: Context) {

    companion object {
        private const val TAG = "WiFiScanner"
    }

    private val context: Context = appContext.applicationContext

    private val wifiManager: WifiManager =
        context.getSystemService(WifiManager::class.java)

    private val executor = ContextCompat.getMainExecutor(context)

    private val _scanResults = MutableSharedFlow<List<ScanResult>>(replay = 1)
    val scanResults: Flow<List<ScanResult>> = _scanResults

    private var isRegistered = false

    private val callback = object : WifiManager.ScanResultsCallback() {
        init {
            Log.v(TAG, "ScanResultsCallback() Initialized")
        }
        override fun onScanResultsAvailable() {
            Log.i(TAG, "onScanResultsAvailable()")
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                _scanResults.tryEmit(wifiManager.scanResults.toList())
            }
        }
    }

    fun start() {
        Log.v(TAG, "start()")
        if (isRegistered) return

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _scanResults.tryEmit(wifiManager.scanResults ?: emptyList())
            wifiManager.registerScanResultsCallback(executor, callback)
            isRegistered = true
        } else {
            isRegistered = false
            return
        }
    }

    fun stop() {
        Log.v(TAG, "stop()")
        if (!isRegistered) return
        wifiManager.unregisterScanResultsCallback(callback)
        isRegistered = false
    }
}