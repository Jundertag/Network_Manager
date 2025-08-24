package com.jayden.networkmanager.features.presentation.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jayden.networkmanager.features.data.main.AndroidDefaultNetworkDetails
import com.jayden.networkmanager.features.data.wifi.AndroidWifiScanner
import com.jayden.networkmanager.features.domain.wifi.AccessPoint
import com.jayden.networkmanager.features.domain.wifi.CurrentAccessPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ApViewModel(
    private val androidWifiScanner: AndroidWifiScanner,
    private val androidDefaultNetworkDetails: AndroidDefaultNetworkDetails
) : ViewModel() {
    private val _selected = MutableStateFlow<AccessPoint?>(null)
    val selected: StateFlow<AccessPoint?> = _selected

    private val _defaultNetwork = MutableStateFlow<AccessPoint?>(null)
    val defaultNetwork = _defaultNetwork.asStateFlow()

    fun select(ap: AccessPoint) { _selected.value = ap }

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private var started = false
    private var wifiScanJob: Job? = null
    private val _results = MutableStateFlow<List<AccessPoint>>(emptyList())
    val results: StateFlow<List<AccessPoint>> = _results.asStateFlow()

    private var defaultNetworkJob: Job? = null
    private val _defaultNetworkDetails = MutableStateFlow<CurrentAccessPoint?>(null)
    val defaultNetworkDetails = _defaultNetworkDetails.asStateFlow()


    fun start() {
        Log.v(TAG, "start()")
        if (started) return
        androidWifiScanner.start()
        androidDefaultNetworkDetails.start()
        started = true

        wifiScanJob = viewModelScope.launch {
            androidWifiScanner.scanResults.collect {
                _results.value = it
                _loading.value = false
            }
        }

        defaultNetworkJob = viewModelScope.launch {
            androidDefaultNetworkDetails.currentWifi.collect {
                _defaultNetworkDetails.value = it
            }
        }
    }

    fun stop() {
        Log.v(TAG, "stop()")
        if (!started) return
        wifiScanJob?.cancel()
        defaultNetworkJob?.cancel()
        androidWifiScanner.stop()
        androidDefaultNetworkDetails.stop()
        started = false
        _loading.value = false
    }

    fun refresh(): Boolean {
        Log.v(TAG, "refresh()")

        val refreshing = androidWifiScanner.startScan()

        _loading.value = refreshing
        return refreshing
    }

    companion object {
        private const val TAG = "ApViewModel"
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ApViewModel(AndroidWifiScanner(context.applicationContext), AndroidDefaultNetworkDetails(context.applicationContext)) as T
                }
            }
    }
}