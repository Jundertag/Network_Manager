package com.jayden.wifimanager.features.models

data class AccessPoint(
    val bssid: String,
    val ssid: String,
    val capabilities: String
)