package com.jayden.networkmanager.ui.fragments.main.connections.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.FragmentConnectionsBinding

class ConnectionsFragment : Fragment(R.layout.fragment_connections) {

    private var _binding: FragmentConnectionsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentConnectionsBinding.bind(view)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}