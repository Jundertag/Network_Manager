package com.jayden.networkmanager.features.models

data class AccessPoint(
    val bssid: String,
    val ssid: String,
    val rssi: Int,
    val capabilities: String
)