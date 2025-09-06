package com.jayden.networkmanager.ui.apawarescan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.FragmentAwareScanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApAwareScanFragment : Fragment(R.layout.fragment_aware_scan) {

    private var _binding: FragmentAwareScanBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAwareScanBinding.bind(view)

        // future usages of binding
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}