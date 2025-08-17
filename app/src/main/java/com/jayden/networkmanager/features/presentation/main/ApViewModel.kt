package com.jayden.networkmanager.features.presentation.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jayden.networkmanager.features.data.wifi.AndroidWifiScanner
import com.jayden.networkmanager.features.domain.wifi.AccessPoint
import com.jayden.networkmanager.features.presentation.apscan.ApScanViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ApViewModel(
    private val androidWifiScanner: AndroidWifiScanner
) : ViewModel() {
    private val _selected = MutableStateFlow<AccessPoint?>(null)
    val selected: StateFlow<AccessPoint?> = _selected

    fun select(ap: AccessPoint) { _selected.value = ap }

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private var collectJob: Job? = null
    private val _results = MutableStateFlow<List<AccessPoint>>(emptyList())
    val results: StateFlow<List<AccessPoint>> = _results.asStateFlow()
    fun start() {
        Log.v(TAG, "start()")

        collectJob = viewModelScope.launch {
            androidWifiScanner.start()
            androidWifiScanner.scanResults.collect {
                _results.value = it
            }
        }
    }

    fun stop() {
        Log.v(TAG, "stop()")
        collectJob?.cancel()
        androidWifiScanner.stop()
    }

    companion object {
        private const val TAG = "ApViewModel"
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ApViewModel(AndroidWifiScanner(context.applicationContext)) as T
                }
            }
    }
}