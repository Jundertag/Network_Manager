package com.jayden.networkmanager.features.domain.wifi

data class CurrentAccessPoint(
    val bssid: String,
    val ssid: String,
    val rssi: Int
)