package com.jayden.wifimanager.features.scan.data

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class WiFiScanner(appContext: Context) {

    private val wifi: WifiManager =
        appContext.applicationContext.getSystemService(WifiManager::class.java)

    private val _scanResults = MutableSharedFlow<List<ScanResult>>(replay = 1)
    val scanResults: Flow<List<ScanResult>> = _scanResults

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                if (ActivityCompat.checkSelfPermission(
                        appContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        appContext as Activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        100
                    )
                    return
                }
                _scanResults.tryEmit(wifi.scanResults)
            }
        }
    }

    fun start(appContext: Context) {
        appContext.registerReceiver(
            receiver,
            IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Context.RECEIVER_EXPORTED
            } else {
                return
            }
        )
    }

    fun stop(appContext: Context) {
        appContext.unregisterReceiver(receiver)
    }
}