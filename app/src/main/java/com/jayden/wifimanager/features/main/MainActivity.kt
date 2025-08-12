package com.jayden.wifimanager.features.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.jayden.wifimanager.R
import com.jayden.wifimanager.databinding.ActivityMainBinding
import com.jayden.wifimanager.features.scan.ui.ApScanFragment
import com.jayden.wifimanager.features.details.ui.ApDetailsFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bottomNav = binding.bottomNav

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_ap_scan -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.fragment_container, ApScanFragment())
                    }
                    true
                }
                R.id.menu_ap_details -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.fragment_container, ApDetailsFragment())
                    }
                    true
                }
                else -> false
            }
        }
        // Default screen should be R.id.menu_app_scan
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.menu_ap_scan
        }
    }
}