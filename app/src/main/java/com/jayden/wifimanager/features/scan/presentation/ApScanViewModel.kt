package com.jayden.wifimanager.features.scan.presentation

import androidx.lifecycle.ViewModel
import com.jayden.wifimanager.features.models.AccessPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ApScanViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<AccessPoint>>(emptyList())
    val items: StateFlow<List<AccessPoint>> = _items

    fun setResults(newList: List<AccessPoint>) {
        _items.value = newList
    }

    fun updateOne(updated: AccessPoint) {
        val map = _items.value.associateBy { it.bssid }.toMutableMap()
        map[updated.bssid] = updated
        _items.value = map.values.toList()
    }

    fun clear() {
        _items.value = emptyList()
    }
}