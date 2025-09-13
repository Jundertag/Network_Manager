package com.jayden.networkmanager.data.mapper

import android.annotation.TargetApi
import android.net.wifi.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import com.jayden.networkmanager.domain.model.wifi.WifiAp

object WifiMapper {

    fun ScanResult.scanResultToDomain(): WifiAp {
        val capabilities = capabilities ?: ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return WifiAp(
                ssid = wifiSsid.toString(),
                bssid = BSSID,
                rssi = level,
                capabilities = capabilities,
                frequency = frequency,
                channelWidth = channelWidth,
                centerFreq0 = centerFreq0,
                centerFreq1 = centerFreq1,
                securityTypes = securityTypes,
                standard = wifiStandard,
            )
        } else {
            @Suppress("DEPRECATION")
            return WifiAp(
                ssid = SSID,
                bssid = BSSID,
                rssi = level,
                capabilities = capabilities,
                frequency = frequency,
                channelWidth = channelWidth,
                centerFreq0 = centerFreq0,
                centerFreq1 = centerFreq1,
                securityTypes = intArrayOf(), // securityTypes is not available below TIRAMISU
                standard = wifiStandard,
            )
        }
    }
}