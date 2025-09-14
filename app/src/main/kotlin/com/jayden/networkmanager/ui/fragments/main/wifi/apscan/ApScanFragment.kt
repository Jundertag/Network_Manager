package com.jayden.networkmanager.ui.fragments.main.wifi.apscan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.FragmentApScanBinding

class ApScanFragment : Fragment(R.layout.fragment_ap_scan) {

    private var _binding: FragmentApScanBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentApScanBinding.bind(view)

        // future usages of binding

    }
}