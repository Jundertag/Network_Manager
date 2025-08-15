package com.jayden.wifimanager.features.main.ui

import android.os.Bundle
import android.view.View
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.jayden.wifimanager.R
import com.jayden.wifimanager.databinding.ActivityMainBinding
import com.jayden.wifimanager.features.details.ui.ApDetailsFragment
import com.jayden.wifimanager.features.scan.ui.ApScanFragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG_SCAN = "frag_scan"
        private const val TAG_DETAILS = "frag_details"
        private const val BACK_STACK_DETAILS = "details_stack"

        private const val TAG = "MainActivity"
    }

    private fun FragmentTransaction.setDefaultAnimations(): FragmentTransaction {
        Log.d(TAG, "setDefaultAnimations()")
        return this.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_right,
            R.anim.slide_in_right,
            R.anim.slide_out_right
        )
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var activeTag: String

    private val backStackListener = OnBackStackChangedListener()


    inner class OnBackStackChangedListener : FragmentManager.OnBackStackChangedListener {
        override fun onBackStackChanged() {}

        override fun onBackStackChangeCommitted(fragment: Fragment, pop: Boolean) {
            if (pop) {
                unblockTouch()
            } else if (supportFragmentManager.backStackEntryCount > 0) {
                blockTouch()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.addOnBackStackChangedListener(backStackListener)

        if (savedInstanceState == null) {
            addFragment(ApScanFragment(), TAG_SCAN)
            binding.fragmentDetailsContainer.visibility = View.GONE
        } else {
            activeTag = savedInstanceState.getString("activeTag", TAG_SCAN)
            val detailsFrag = supportFragmentManager.findFragmentByTag(TAG_DETAILS)
            if (detailsFrag != null && detailsFrag.isVisible && supportFragmentManager.findFragmentById(
                    R.id.fragment_details_container
                ) == detailsFrag
            ) {
                binding.fragmentDetailsContainer.visibility = View.VISIBLE
            } else {
                binding.fragmentDetailsContainer.visibility = View.GONE
            }
        }


        binding.bottomNav.setOnItemSelectedListener { item ->
            val targetTag = when (item.itemId) {
                R.id.menu_ap_scan    -> TAG_SCAN
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("activeTag", activeTag)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.removeOnBackStackChangedListener(backStackListener)
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        Log.d(TAG, "addFragment($fragment, $tag)")
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container, fragment, tag)
        }
        activeTag = tag
    }

    private fun showFragment(tag: String) {
        Log.d(TAG, "showFragment($tag)")
        val activeFragment = supportFragmentManager.findFragmentByTag(activeTag)
        val targetFragment = supportFragmentManager.findFragmentByTag(tag) ?: throw IllegalStateException("Tag cannot be null")

        if (targetFragment.isVisible) return
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            if (activeFragment != null && activeFragment != targetFragment) {
                hide(activeFragment)
                addToBackStack(activeTag)
            }
            show(targetFragment)
        }
        activeTag = tag
    }

    private fun hideFragment(tag: String) {
        Log.d(TAG, "hideFragment($tag)")
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                hide(fragment)
            }
        }
    }

    private fun showDetailFragment(tag: String) {
        Log.d(TAG, "showDetailFragment($tag)")

        if (supportFragmentManager.findFragmentByTag(tag)?.isAdded == true) return

        binding.fragmentDetailsContainer.visibility = View.VISIBLE

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            setDefaultAnimations()
            add(R.id.fragment_details_container, ApDetailsFragment(), tag)
            addToBackStack(BACK_STACK_DETAILS)
        }
    }

    private fun blockTouch() {
        Log.d(TAG, "blockTouch()")
        binding.fragmentDetailsContainer.apply {
            isClickable = true
            isFocusable = true
        }
    }

    private fun unblockTouch() {
        Log.d(TAG, "unblockTouch()")
        binding.fragmentDetailsContainer.apply {
            isClickable = false
            isFocusable = false
        }
    }

    fun showApDetails() {
        Log.d(TAG, "showApDetails()")
        showDetailFragment(TAG_DETAILS)
    }
}