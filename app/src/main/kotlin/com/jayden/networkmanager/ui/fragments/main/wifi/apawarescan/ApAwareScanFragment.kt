package com.jayden.networkmanager.ui.fragments.main.wifi.apawarescan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.FragmentApAwareScanBinding

class ApAwareScanFragment : Fragment(R.layout.fragment_ap_aware_scan) {

    private var _binding: FragmentApAwareScanBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentApAwareScanBinding.bind(view)

        // future usages of binding
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}