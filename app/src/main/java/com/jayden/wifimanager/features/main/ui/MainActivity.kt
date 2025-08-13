package com.jayden.wifimanager.features.main.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.jayden.wifimanager.R
import com.jayden.wifimanager.databinding.ActivityMainBinding
import com.jayden.wifimanager.features.details.ui.ApDetailsFragment
import com.jayden.wifimanager.features.scan.ui.ApScanFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG_SCAN = "frag_scan"
        private const val TAG_DETAILS = "frag_details"

        private const val TAG = "MainActivity"
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var activeTag: String = TAG_SCAN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val scan: ApScanFragment? = supportFragmentManager.findFragmentByTag(TAG_SCAN) as? ApScanFragment
        val details: ApDetailsFragment? = supportFragmentManager.findFragmentByTag(TAG_DETAILS) as? ApDetailsFragment
        Log.v(TAG, "$scan $details")

        if (scan == null || details == null) {
            val scan = scan ?: ApScanFragment()
            val details = details ?: ApDetailsFragment()

            Log.v(TAG, "$scan $details")

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container, scan, TAG_SCAN)
                add(R.id.fragment_container, details, TAG_DETAILS)
                hide(details)
            }
            activeTag = TAG_SCAN
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            val targetTag = when (item.itemId) {
                R.id.menu_ap_scan    -> TAG_SCAN
                R.id.menu_ap_details -> TAG_DETAILS
                else -> return@setOnItemSelectedListener false
            }
            if (targetTag == activeTag) return@setOnItemSelectedListener true
            showFragment(targetTag)
            true
        }

        if (savedInstanceState == null) {
            binding.bottomNav.selectedItemId = R.id.menu_ap_scan
        }
    }

    private fun showFragment(fragment: String) {
        Log.d(TAG, "showFragment($fragment)")
        val activeFragment = supportFragmentManager.findFragmentByTag(activeTag) ?: return
        val targetFragment = supportFragmentManager.findFragmentByTag(fragment) ?: return
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            hide(activeFragment)
            show(targetFragment)
        }
        activeTag = fragment
    }

    fun showDetailsTab() {
        binding.bottomNav.selectedItemId = R.id.menu_ap_details
    }
}