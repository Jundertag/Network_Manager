package com.jayden.networkmanager.ui.connections

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.FragmentScanBinding

class ConnectionsFragment : Fragment(R.layout.fragment_connections) {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScanBinding.bind(view)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}