package com.jayden.networkmanager.ui.apscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.FragmentScanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApScanFragment : Fragment(R.layout.fragment_scan) {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScanBinding.bind(view)

        // future usages of binding
        // binding.scanlist.adapter = ...
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}