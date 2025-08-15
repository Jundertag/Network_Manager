package com.jayden.wifimanager.features.scan.data

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
class WiFiScanner(appContext: Context) {

    companion object {
        private const val TAG = "WiFiScanner"
    }

    private val context: Context = appContext.applicationContext

    private val wifi: WifiManager =
        context.getSystemService(WifiManager::class.java)

    private val _scanResults = MutableSharedFlow<List<ScanResult>>(replay = 1)
    val scanResults: Flow<List<ScanResult>> = _scanResults

    private var isRegistered = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    _scanResults.tryEmit(wifi.scanResults ?: emptyList())
                }
            }

            if (intent.action == WifiManager.RSSI_CHANGED_ACTION) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    _scanResults.tryEmit(wifi.scanResults ?: emptyList())
                }
            }
        }
    }

    fun start() {
        Log.d(TAG, "start()")
        if (isRegistered) return

        val filter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            context.registerReceiver(receiver, filter)
        }
        isRegistered = true

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _scanResults.tryEmit(wifi.scanResults ?: emptyList())
        } else {
            return
        }
    }

    fun stop() {
        Log.d(TAG, "stop()")
        if (!isRegistered) return
        runCatching { context.unregisterReceiver(receiver) }
        isRegistered = false
    }
}