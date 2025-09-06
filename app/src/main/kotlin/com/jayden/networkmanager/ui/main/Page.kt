package com.jayden.networkmanager.ui.main

import com.jayden.networkmanager.R

sealed class Page(val id: Long, val navId: Int) {
    data object Scan : Page(0, R.id.menu_ap_scan)
}