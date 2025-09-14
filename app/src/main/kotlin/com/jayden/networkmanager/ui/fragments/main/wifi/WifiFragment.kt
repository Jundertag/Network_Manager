package com.jayden.networkmanager.ui.fragments.main.wifi

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.FragmentWifiBinding

class WifiFragment : Fragment(R.layout.fragment_wifi) {

    private var _binding: FragmentWifiBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "WifiFragment"
        private const val AP_SCAN = "ApScanFragment"
        private const val AP_AWARE_SCAN = "ApAwareScanFragment"
        private const val AP_DIRECT_SCAN = "ApDirectScanFragment"
    }

    private val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            binding.pager.currentItem = tab?.position ?: 0
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            when (state) {
                ViewPager2.SCROLL_STATE_IDLE -> {
                    val tab = binding.tabLayout.getTabAt(binding.pager.currentItem)
                    binding.tabLayout.selectTab(tab)
                }

                ViewPager2.SCROLL_STATE_DRAGGING -> {

                }

                ViewPager2.SCROLL_STATE_SETTLING -> {

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWifiBinding.bind(view)

        val paddingStart = binding.tabLayout.paddingStart
        val paddingEnd = binding.tabLayout.paddingEnd
        val paddingBottom = binding.tabLayout.paddingBottom
        val paddingTop = binding.tabLayout.paddingTop

        ViewCompat.setOnApplyWindowInsetsListener(binding.tabLayout) { view, insets ->
            val topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            binding.tabLayout.setPadding(
                paddingStart,
                paddingTop + topInset,
                paddingEnd,
                paddingBottom
            )

            insets
        }

        binding.tabLayout.apply {
            addTab(binding.tabLayout.newTab().setText(R.string.menu_ap_scan).setTag(AP_SCAN))
            addTab(binding.tabLayout.newTab().setText(R.string.menu_ap_aware_scan).setTag(AP_AWARE_SCAN))
            addTab(binding.tabLayout.newTab().setText(R.string.menu_ap_direct_scan).setTag(AP_DIRECT_SCAN))
            tabGravity = TabLayout.GRAVITY_CENTER
        }

        binding.tabLayout.addOnTabSelectedListener(tabSelectedListener)
        binding.pager.registerOnPageChangeCallback(pageChangeCallback)

        binding.pager.apply {
            adapter = PagerAdapter(this@WifiFragment)
        }

    }

    override fun onDestroyView() {
        binding.tabLayout.removeOnTabSelectedListener(tabSelectedListener)
        binding.pager.unregisterOnPageChangeCallback(pageChangeCallback)
        _binding = null
        super.onDestroyView()
    }
}