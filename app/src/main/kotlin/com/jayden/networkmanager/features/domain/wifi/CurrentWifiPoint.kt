package com.jayden.networkmanager.features.domain.wifi

data class CurrentWifiPoint(
    val bssid: String,
    val ssid: String,
    val rssi: Int
)