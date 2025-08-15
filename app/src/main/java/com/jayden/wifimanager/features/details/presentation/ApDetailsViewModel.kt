package com.jayden.wifimanager.features.details.presentation

import androidx.lifecycle.ViewModel
import com.jayden.wifimanager.features.details.data.WiFiDetails
import com.jayden.wifimanager.features.details.ui.ApDetailsFragment

class ApDetailsViewModel(
    private val wifiDetails: WiFiDetails
) : ViewModel() {

    val activeNetwork = wifiDetails.activeNetwork
    val capabilities = wifiDetails.capabilities
}