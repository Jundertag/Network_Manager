package com.jayden.networkmanager.features.data.wifi

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

    private val callback = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.v(TAG, "onReceive($context, $intent)")
            if (intent?.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                if (ContextCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
                    _scanResults.tryEmit(wifiManager.scanResults ?: emptyList())
                }
            }
        }
    }

    fun start() {
        Log.v(TAG, "start()")
        if (isRegistered) return

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _scanResults.tryEmit(wifiManager.scanResults ?: emptyList())
            context.registerReceiver(callback, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            isRegistered = true
        } else {
            isRegistered = false
            return
        }
    }

    fun stop() {
        Log.v(TAG, "stop()")
        if (!isRegistered) return
        isRegistered = false
    }

    fun refresh() {
        Log.v(TAG, "refresh()")
        if (!isRegistered) return
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            wifiManager.startScan()
        }
    }
}