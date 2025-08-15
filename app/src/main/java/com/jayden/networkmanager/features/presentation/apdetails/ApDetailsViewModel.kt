package com.jayden.networkmanager.features.presentation.apdetails

import androidx.lifecycle.ViewModel
import com.jayden.networkmanager.features.data.wifi.WiFiDetails

class ApDetailsViewModel(
    private val wifiDetails: WiFiDetails
) : ViewModel() {

    val activeNetwork = wifiDetails.activeNetwork
    val capabilities = wifiDetails.capabilities
}