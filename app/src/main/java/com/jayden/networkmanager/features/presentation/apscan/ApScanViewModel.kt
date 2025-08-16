package com.jayden.networkmanager.features.presentation.apscan

import android.content.Context
import android.net.wifi.ScanResult
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jayden.networkmanager.features.data.wifi.WiFiScanner
import com.jayden.networkmanager.features.datamodels.wifi.AccessPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ApScanViewModel(
    private val wiFiScanner: WiFiScanner
) : ViewModel() {

    private val _items = MutableStateFlow<List<AccessPoint>>(emptyList())
    private val _scanning = MutableStateFlow(false)

    val items: StateFlow<List<AccessPoint>> = _items.asStateFlow()
    val scanning: StateFlow<Boolean> = _scanning.asStateFlow()

    private var collectJob: Job? = null

    fun start() {
        Log.v(TAG, "start()")
        if (_scanning.value) return
        wiFiScanner.start()
        _scanning.value = true
        collectJob = viewModelScope.launch {
            wiFiScanner.scanResults.collectLatest { results ->
                // new results arrived -> update list and mark scan done
                _items.value = results.map { it.toAp() }
            }
        }
    }

    fun stop() {
        Log.v(TAG, "stop()")
        collectJob?.cancel()
        wiFiScanner.stop()
        _scanning.value = false
    }

    fun refresh() {
        Log.v(TAG, "refresh()")
        wiFiScanner.refresh()
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
                ssid = SSID.ifBlank { "<Hidden SSID>" },
                bssid = BSSID ?: "",
                rssi = level,
                capabilities = capabilities
            )
        }
    }

    companion object {
        private const val TAG = "ApScanViewModel"
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ApScanViewModel(WiFiScanner(context.applicationContext)) as T
                }
            }
    }
}