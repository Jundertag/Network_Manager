package com.jayden.networkmanager.features.details.presentation

import androidx.lifecycle.ViewModel
import com.jayden.networkmanager.features.details.data.WiFiDetails

class ApDetailsViewModel(
    private val wifiDetails: WiFiDetails
) : ViewModel() {

    val activeNetwork = wifiDetails.activeNetwork
    val capabilities = wifiDetails.capabilities
}