package com.jayden.networkmanager.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jayden.networkmanager.ui.apawarescan.ApAwareScanFragment
import com.jayden.networkmanager.ui.apscan.ApScanFragment
import com.jayden.networkmanager.ui.connections.ConnectionsFragment

class PagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    private val pages: List<Page> = listOf(Page.Connections, Page.WifiScan, Page.WifiAwareScan)

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment = when (pages[position]) {
        is Page.Connections -> ConnectionsFragment()
        is Page.WifiScan -> ApScanFragment()
        is Page.WifiAwareScan -> ApAwareScanFragment()
    }

    override fun getItemId(position: Int): Long = pages[position].id
    override fun containsItem(itemId: Long): Boolean = pages.any { it.id == itemId }

    fun getItemIdFromNav(navId: Int): Long = pages.first { it.navId == navId }.id

    fun getItemPos(navId: Int): Int = pages.indexOfFirst { it.navId == navId }

    fun getItemNavId(position: Int): Int = pages[position].navId
}