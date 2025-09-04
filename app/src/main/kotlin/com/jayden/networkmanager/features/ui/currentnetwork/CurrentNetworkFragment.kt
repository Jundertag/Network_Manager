package com.jayden.networkmanager.features.ui.currentnetwork

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jayden.networkmanager.features.presentation.currentnetwork.CurrentNetworkViewModel
import com.jayden.networkmanager.databinding.FragmentCurrentNetworkBinding

class CurrentNetworkFragment : Fragment() {

    companion object {
        private const val TAG = "CurrentNetworkFragment"
    }

    private var _binding: FragmentCurrentNetworkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CurrentNetworkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView($inflater: LayoutInflater, $container: ViewGroup?, $savedInstanceState: Bundle?)")
        _binding = FragmentCurrentNetworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated($view: View, $savedInstanceState: Bundle?)")
        super.onViewCreated(view, savedInstanceState)
        // Set up UI elements and listeners here

    }

}