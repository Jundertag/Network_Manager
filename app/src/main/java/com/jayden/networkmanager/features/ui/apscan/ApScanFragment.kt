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
import com.jayden.networkmanager.databinding.FragmentApScanBinding
import com.jayden.networkmanager.features.presentation.apscan.ApScanViewModel
import com.jayden.networkmanager.features.presentation.main.ApViewModel
import com.jayden.networkmanager.features.ui.apscan.ApAdapter
import com.jayden.networkmanager.features.ui.main.MainActivity
import com.jayden.networkmanager.features.ui.permissions.PermissionHelper
import kotlinx.coroutines.launch

class ApScanFragment : Fragment() {

    companion object {
        private const val TAG = "ApScanFragment"
    }

    private val apViewModel: ApViewModel by activityViewModels()
    private var _binding: FragmentApScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var permissionHelper: PermissionHelper

    private val vm: ApScanViewModel by viewModels {
        ApScanViewModel.Companion.factory(requireContext().applicationContext)
    }
    private lateinit var adapter: ApAdapter

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

        permissionHelper = PermissionHelper(
            requireContext(),
            this,
            shouldShowRationale = { permission ->
                requireActivity().shouldShowRequestPermissionRationale(permission)
            }
        )

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
        Log.v(TAG, "onStart()")
        super.onStart()

        val perms = arrayOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.ACCESS_COARSE_LOCATION
                Manifest.permission.ACCESS_FINE_LOCATION
                Manifest.permission.NEARBY_WIFI_DEVICES
            } else {
                Manifest.permission.ACCESS_COARSE_LOCATION
                Manifest.permission.ACCESS_FINE_LOCATION
            }
        )

        permissionHelper.request(perms) { callback ->
            if (callback.granted.containsAll(callback.raw.keys)) {
                vm.start()
            } else { /* trigger on screen message */
                Toast.makeText(requireContext(), "Permissions Denied, Results will not be accurate", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Permissions Denied")
            }
        }
    }

    override fun onStop() {
        Log.v(TAG, "onStop()")
        super.onStop()
        vm.stop()
    }

    override fun onDestroyView() {
        Log.v(TAG, "onDestroyView()")
        _binding = null
        super.onDestroyView()
    }
}