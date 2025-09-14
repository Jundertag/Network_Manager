package com.jayden.networkmanager.ui.activities.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jayden.networkmanager.ui.fragments.main.wifi.WifiFragment
import com.jayden.networkmanager.ui.fragments.main.connections.main.ConnectionsFragment
import com.jayden.networkmanager.ui.fragments.main.vpn.VpnFragment
import com.jayden.networkmanager.ui.main.Page

class PagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    private val pages: List<Page> = listOf(Page.Connections, Page.WifiScan, Page.VPN)

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment = when (pages[position]) {
        Page.Connections -> ConnectionsFragment()
        Page.WifiScan -> WifiFragment()
        Page.VPN -> VpnFragment()
    }

    override fun getItemId(position: Int): Long = pages[position].id
    override fun containsItem(itemId: Long): Boolean = pages.any { it.id == itemId }

    fun getItemIdFromNav(navId: Int): Long = pages.first { it.navId == navId }.id

    fun getItemPos(navId: Int): Int = pages.indexOfFirst { it.navId == navId }

    fun getItemNavId(position: Int): Int = pages[position].navId
}