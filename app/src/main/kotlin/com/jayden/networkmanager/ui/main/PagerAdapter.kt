package com.jayden.networkmanager.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jayden.networkmanager.ui.apawarescan.ApAwareScanFragment
import com.jayden.networkmanager.ui.apscan.ApScanFragment
import dagger.hilt.android.scopes.ActivityScoped
import jakarta.inject.Inject

@ActivityScoped
class PagerAdapter @Inject constructor(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    private val pages: List<Page> = listOf(Page.Scan)

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment = when (pages[position]) {
        is Page.Scan -> ApScanFragment()
        is Page.AwareScan -> ApAwareScanFragment()
    }

    override fun getItemId(position: Int): Long = pages[position].id
    override fun containsItem(itemId: Long): Boolean = pages.any { it.id == itemId }
}