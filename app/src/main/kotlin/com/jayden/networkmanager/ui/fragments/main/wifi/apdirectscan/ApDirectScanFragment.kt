package com.jayden.networkmanager.ui.fragments.main.wifi.apdirectscan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.FragmentApDirectScanBinding

class ApDirectScanFragment : Fragment(R.layout.fragment_ap_direct_scan) {

    private var _binding: FragmentApDirectScanBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentApDirectScanBinding.bind(view)

        // future usages of binding
    }
}