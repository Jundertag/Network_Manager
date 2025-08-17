package com.jayden.networkmanager.features.ui.apscan

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jayden.networkmanager.databinding.FragmentApScanBinding
import com.jayden.networkmanager.features.presentation.apscan.ApScanViewModel
import com.jayden.networkmanager.features.presentation.main.ApViewModel
import com.jayden.networkmanager.features.ui.apscan.ApAdapter
import com.jayden.networkmanager.features.ui.main.MainActivity
import com.jayden.networkmanager.features.ui.permissions.PermissionManager
import kotlinx.coroutines.launch

class ApScanFragment : Fragment() {

    companion object {
        private const val TAG = "ApScanFragment"
    }

    private val apViewModel: ApViewModel by activityViewModels {
        ApViewModel.Companion.factory(requireContext().applicationContext)
    }
    private var _binding: FragmentApScanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApScanViewModel by viewModels {
        ApScanViewModel.Companion.factory(requireContext().applicationContext)
    }
    private lateinit var adapter: ApAdapter
    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.NEARBY_WIFI_DEVICES
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private val permissionManager by lazy {
        PermissionManager(
            activity = requireActivity(),
            context = requireContext(),
            permissionResult = { permissions ->
                val granted = permissions.values.all { it }
                if (granted) {
                    apViewModel.start()
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.v(TAG, "onCreateView($inflater: LayoutInflater, $container: ViewGroup?, $savedInstanceState: Bundle?): View")
        _binding = FragmentApScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated($view: View, $savedInstanceState: Bundle?)")
        adapter = ApAdapter { ap ->
            apViewModel.select(ap)
            (requireActivity() as? MainActivity)?.showApDetails()
        }

        binding.apScanRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@ApScanFragment.adapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { apViewModel.results.collect { adapter.submitList(it) } }
                launch { viewModel.loading.collect { binding.swipeRefresh.isRefreshing = it } }
            }
        }

        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefresh
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        swipeRefreshLayout.setOnChildScrollUpCallback { _, child ->
            child?.canScrollVertically(-1) == true
        }

    }

    override fun onStart() {
        Log.v(TAG, "onStart()")
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launch {
            permissionManager.requestPermissions(permissions)
        }
    }

    override fun onStop() {
        Log.v(TAG, "onStop()")
        super.onStop()
        apViewModel.stop()
    }

    override fun onDestroyView() {
        Log.v(TAG, "onDestroyView()")
        _binding = null
        super.onDestroyView()
    }
}