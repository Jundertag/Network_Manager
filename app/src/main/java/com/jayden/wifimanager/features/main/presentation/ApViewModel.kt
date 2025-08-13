package com.jayden.wifimanager.features.main.presentation

import androidx.lifecycle.ViewModel
import com.jayden.wifimanager.features.models.AccessPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ApViewModel : ViewModel() {
    private val _selected = MutableStateFlow<AccessPoint?>(null)
    val selected: StateFlow<AccessPoint?> = _selected

    fun select(ap: AccessPoint) { _selected.value = ap }
}