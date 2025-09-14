package com.jayden.networkmanager.ui.fragments.main.wifi

import com.jayden.networkmanager.R

sealed class Page(val id: Long, val navId: Int) {

    data object ApScan : Page(0, R.id.menu_wifi_scan)
    data object ApAwareScan : Page(1, R.id.menu_wifi_aware_scan)
    data object ApDirectScan : Page(2, R.id.menu_wifi_direct_scan)
}