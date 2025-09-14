package com.jayden.networkmanager.ui.fragments.main.vpn

import com.jayden.networkmanager.R

sealed class Page(val id: Long, val navId: Int) {

    data object VpnManage : Page(0, R.id.menu_vpn_manage)
    data object VpnPackets : Page(1, R.id.menu_vpn_packets)
}