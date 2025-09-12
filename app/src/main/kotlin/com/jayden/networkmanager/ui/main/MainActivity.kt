package com.jayden.networkmanager.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.ActivityMainBinding
import com.jayden.networkmanager.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

class MainActivity : AppCompatActivity(
    R.layout.activity_main
) {
    private val pagerAdapter = PagerAdapter(this)

    private val viewModel: MainViewModel by viewModels()
    companion object {
        private const val TAG = "MainActivity"
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            when (state) {
                ViewPager2.SCROLL_STATE_IDLE -> {
                    binding.navBar.selectedItemId =
                        pagerAdapter.getItemNavId(binding.viewPager.currentItem)
                }

                ViewPager2.SCROLL_STATE_DRAGGING -> {

                }

                ViewPager2.SCROLL_STATE_SETTLING -> {

                }
            }
        }
    }

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.navBar.setOnItemSelectedListener {
            Log.v(TAG, "NavigationBarView.OnItemSelectedListener: ${it.itemId}")
            binding.viewPager.currentItem = pagerAdapter.getItemPos(it.itemId)
            true
        }

        binding.viewPager.adapter = PagerAdapter(this)
        binding.viewPager.offscreenPageLimit = 1

        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}