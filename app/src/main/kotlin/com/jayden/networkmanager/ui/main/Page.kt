package com.jayden.networkmanager.ui.main

import com.jayden.networkmanager.R

sealed class Page(val id: Long, val navId: Int) {
    data object Connections : Page(0, R.id.menu_connections)
    data object WifiScan : Page(1, R.id.menu_ap_scan)
    data object WifiAwareScan : Page(2, R.id.menu_ap_aware_scan)
}