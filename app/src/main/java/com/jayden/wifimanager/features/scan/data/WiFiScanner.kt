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
                    Log.v(TAG, wifi.scanResults.toString())
                }
            }
        }
    }

    fun start() {
        if (isRegistered) return
        val filter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            context.registerReceiver(receiver, filter)
        }
        isRegistered = true
    }

    fun stop() {
        if (!isRegistered) return
        runCatching { context.unregisterReceiver(receiver) }
        isRegistered = false
    }

    fun requestScan(): Boolean {
        // Best-effort; might be throttled or ignored
        @Suppress("DEPRECATION")
        return runCatching { wifi.startScan() }.getOrDefault(false)
    }
}