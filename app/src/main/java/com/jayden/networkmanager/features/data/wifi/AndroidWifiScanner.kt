package com.jayden.networkmanager.features.data.wifi

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
import com.jayden.networkmanager.features.domain.wifi.AccessPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.apply

class AndroidWifiScanner(appContext: Context) {

    companion object {
        private const val TAG = "AndroidWifiScanner"
    }

    private val context = appContext.applicationContext
    private val wifiManager = context.getSystemService(WifiManager::class.java)

    private val _scanResults = MutableStateFlow<List<AccessPoint>>(emptyList())
    val scanResults: Flow<List<AccessPoint>> = _scanResults

    private val filter = IntentFilter().apply {
        addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        addAction(WifiManager.RSSI_CHANGED_ACTION)
    }

    private var isRegistered = false

    fun startScan(): Boolean {
        Log.v(TAG, "startScan()")
        return wifiManager.startScan()
    }

    fun start() {
        Log.v(TAG, "start()")
        if (isRegistered) return
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        isRegistered = true

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _scanResults.tryEmit(wifiManager.scanResults.map { it.toAp() })
        }
    }

    fun stop() {
        Log.v(TAG, "stop()")
        if (!isRegistered) return
        context.unregisterReceiver(receiver)
        isRegistered = false
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.v(TAG, "onReceive($context: Context, $intent: Intent)")

            if (intent.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {

                if (intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        _scanResults.tryEmit(wifiManager.scanResults.map { it.toAp() })
                    }
                }
            }

            if (intent.action == WifiManager.RSSI_CHANGED_ACTION) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    _scanResults.tryEmit(wifiManager.scanResults.map { it.toAp() })
                }
            }
        }
    }

    private fun ScanResult.toAp(): AccessPoint {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AccessPoint(
                ssid = wifiSsid?.toString()?.removeSurrounding("\"")?.ifBlank { "<Hidden SSID>" }
                    ?: "<Hidden SSID>",
                bssid = BSSID ?: "",
                rssi = level,
                capabilities = capabilities
            )
        } else {
            @Suppress("DEPRECATION")
            AccessPoint(
                ssid = SSID.ifBlank { "<Hidden SSID>" } ?: "<Hidden SSID>",
                bssid = BSSID ?: "",
                rssi = level,
                capabilities = capabilities
            )
        }
    }
}