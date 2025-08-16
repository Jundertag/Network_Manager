package com.jayden.networkmanager.features.datamodels.wifi

data class AccessPoint(
    val bssid: String,
    val ssid: String,
    val rssi: Int,
    val capabilities: String
)