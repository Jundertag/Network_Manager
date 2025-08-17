package com.jayden.networkmanager.features.presentation.apscan

import android.content.Context
import android.net.wifi.ScanResult
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jayden.networkmanager.features.data.wifi.AndroidWifiScanner
import com.jayden.networkmanager.features.domain.wifi.AccessPoint
import com.jayden.networkmanager.features.presentation.main.ApViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ApScanViewModel(
    private val androidWifiScanner: AndroidWifiScanner
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _results = MutableStateFlow<List<AccessPoint>>(emptyList())
    val results: StateFlow<List<AccessPoint>> = _results.asStateFlow()

    fun refresh() {
        Log.v(TAG, "refresh()")
        _loading.value = androidWifiScanner.startScan()
        viewModelScope.launch {
            androidWifiScanner.scanResults.collectLatest {
                _results.value = it
                _loading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "ApScanViewModel"
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ApScanViewModel(AndroidWifiScanner(context.applicationContext)) as T
                }
            }
    }
}