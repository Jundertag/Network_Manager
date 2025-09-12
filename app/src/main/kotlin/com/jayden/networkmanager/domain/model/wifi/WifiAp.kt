package com.jayden.networkmanager.domain.model.wifi

data class WifiAp(
    val ssid: String?,
    val bssid: String,
    val rssi: Int,
    val capabilities: String,
    val frequency: Int,
    val channelWidth: Int,
    val centerFreq0: Int?,
    val centerFreq1: Int?,
    val securityTypes: IntArray,
    val standard: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WifiAp

        if (rssi != other.rssi) return false
        if (ssid != other.ssid) return false
        if (bssid != other.bssid) return false
        if (!securityTypes.contentEquals(other.securityTypes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rssi
        result = 31 * result + (ssid?.hashCode() ?: 0)
        result = 31 * result + bssid.hashCode()
        result = 31 * result + securityTypes.contentHashCode()
        return result
    }
}