package com.jayden.networkmanager.ui.fragments.main.wifi

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jayden.networkmanager.ui.fragments.main.wifi.apawarescan.ApAwareScanFragment
import com.jayden.networkmanager.ui.fragments.main.wifi.apdirectscan.ApDirectScanFragment
import com.jayden.networkmanager.ui.fragments.main.wifi.apscan.ApScanFragment

class PagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private val pages: List<Page> = listOf(Page.ApScan, Page.ApAwareScan, Page.ApDirectScan)

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment = when (pages[position]) {
        Page.ApScan -> ApScanFragment()
        Page.ApAwareScan -> ApAwareScanFragment()
        Page.ApDirectScan -> ApDirectScanFragment()
    }

    override fun getItemId(position: Int): Long = pages[position].id
    override fun containsItem(itemId: Long): Boolean = pages.any { it.id == itemId }

    fun getItemIdFromNav(navId: Int): Long = pages.first { it.navId == navId }.id

    fun getItemPos(navId: Int): Int = pages.indexOfFirst { it.navId == navId }

    fun getItemNavId(position: Int): Int = pages[position].navId
}