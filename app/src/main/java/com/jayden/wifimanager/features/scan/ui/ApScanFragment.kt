package com.jayden.wifimanager.features.scan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jayden.wifimanager.features.scan.data.WiFiScanner
import com.jayden.wifimanager.databinding.FragmentApScanBinding
import com.jayden.wifimanager.features.scan.presentation.ApScanViewModel
import kotlinx.coroutines.launch

class ApScanFragment : Fragment() {

    private var _binding: FragmentApScanBinding? = null
    private val binding get() = _binding!!

    private val vm: ApScanViewModel by viewModels()

    private val wifiScanner = WiFiScanner(requireContext().applicationContext)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ApAdapter { clicked -> /* onClick */ }

        binding.apScanRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.apScanRecycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.items.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}