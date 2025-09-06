package com.jayden.networkmanager.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(
    R.layout.activity_main
) {
    @Inject lateinit var pagerAdapter: PagerAdapter

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.navBar.selectedItemId = pagerAdapter.getItemId(position).toInt()
        }
    }

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.navBar.setOnItemSelectedListener {
            binding.viewPager.currentItem = it.itemId
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