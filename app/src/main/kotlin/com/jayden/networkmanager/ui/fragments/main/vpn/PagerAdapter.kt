package com.jayden.networkmanager.ui.fragments.main.vpn

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jayden.networkmanager.ui.fragments.main.vpn.manage.VpnManageFragment
import com.jayden.networkmanager.ui.fragments.main.vpn.packets.VpnPacketsFragment

class PagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private val pages: List<Page> = listOf(Page.VpnManage, Page.VpnPackets)

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment = when(pages[position]) {
        Page.VpnManage -> VpnManageFragment()
        Page.VpnPackets -> VpnPacketsFragment()
    }


}