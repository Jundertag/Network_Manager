package com.jayden.wifimanager.features.scan.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jayden.wifimanager.databinding.FragmentApScanBinding
import com.jayden.wifimanager.features.main.MainActivity
import com.jayden.wifimanager.features.scan.presentation.ApScanViewModel
import com.jayden.wifimanager.features.main.PermissionHelper
import com.jayden.wifimanager.features.main.presentation.ApViewModel
import kotlinx.coroutines.launch


class ApScanFragment : Fragment() {

    private val apViewModel: ApViewModel by activityViewModels()
    private var _binding: FragmentApScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var permissionHelper: PermissionHelper

    private val vm: ApScanViewModel by viewModels {
        ApScanViewModel.factory(requireContext().applicationContext)
    }
    private lateinit var adapter: ApAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ApAdapter { ap ->
            apViewModel.select(ap)
            (requireActivity() as? MainActivity)?.showDetailsTab()
        }

        permissionHelper = PermissionHelper(
            requireContext(),
            this
        ) { permission -> shouldShowRequestPermissionRationale(permission) }

        binding.apScanRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ApScanFragment.adapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.items.collect { list ->
                    adapter.submitList(list)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        vm.start()
    }

    override fun onStop() {
        super.onStop()
        vm.stop()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}