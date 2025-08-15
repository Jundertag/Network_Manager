package com.jayden.networkmanager.features.presentation.main

import androidx.lifecycle.ViewModel
import com.jayden.networkmanager.features.datamodels.AccessPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ApViewModel : ViewModel() {
    private val _selected = MutableStateFlow<AccessPoint?>(null)
    val selected: StateFlow<AccessPoint?> = _selected

    fun select(ap: AccessPoint) { _selected.value = ap }
}