package com.jayden.networkmanager.ui.activities.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.ActivityMainBinding
import com.jayden.networkmanager.presentation.main.MainViewModel

class MainActivity : AppCompatActivity(
    R.layout.activity_main
) {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val pagerAdapter = PagerAdapter(this)

    private val viewModel: MainViewModel by viewModels()
    companion object {
        private const val TAG = "MainActivity"
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            when (state) {
                ViewPager2.SCROLL_STATE_IDLE -> {
                    binding.navBar.selectedItemId = pagerAdapter.getItemNavId(binding.viewPager.currentItem)
                }

                ViewPager2.SCROLL_STATE_DRAGGING -> {

                }

                ViewPager2.SCROLL_STATE_SETTLING -> {

                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.navBar.setOnItemSelectedListener {
            Log.v(TAG, "NavigationBarView.OnItemSelectedListener: ${it.itemId}")

            try {
                binding.viewPager.currentItem = pagerAdapter.getItemPos(it.itemId)
                true
            } catch (e: Exception) {
                Log.w(TAG, "Error: $e")
                false
            }
        }

        binding.viewPager.adapter = PagerAdapter(this)
        binding.viewPager.offscreenPageLimit = 1

        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)
    }

    override fun onDestroy() {
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroy()
    }
}