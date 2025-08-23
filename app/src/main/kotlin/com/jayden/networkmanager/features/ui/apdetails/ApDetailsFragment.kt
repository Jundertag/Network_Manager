package com.jayden.networkmanager.features.ui.apdetails

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jayden.networkmanager.R
import com.jayden.networkmanager.databinding.FragmentApDetailsBinding
import com.jayden.networkmanager.features.domain.wifi.AccessPoint
import com.jayden.networkmanager.features.presentation.apdetails.ApDetailsViewModel
import com.jayden.networkmanager.features.presentation.main.ApViewModel
import kotlinx.coroutines.launch

class ApDetailsFragment : Fragment() {

    companion object {
        private const val TAG = "ApDetailsFragment"
    }

    private val apViewModel: ApViewModel by activityViewModels()

    private val apDetailsViewModel: ApDetailsViewModel by viewModels()

    private var _binding: FragmentApDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.v(TAG, "onCreateView($inflater: LayoutInflater, $container: ViewGroup?, $savedInstanceState: Bundle?): View")
        _binding = FragmentApDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated($view: View, $savedInstanceState: Bundle?)")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                apViewModel.selected.collect { ap ->
                    ap?.let { render(it) }
                }
            }
        }
    }

    override fun onStart() {
        Log.v(TAG, "onStart()")
        super.onStart()
        apViewModel.start()
    }

    override fun onStop() {
        Log.v(TAG, "onStop()")
        super.onStop()
        apViewModel.stop()
    }

    private fun render(ap: AccessPoint?) {
        Log.v(TAG, "render($ap): AccessPoint?")
        if (ap != null) {
            binding.title.text = ap.ssid
            binding.bssidData.text = ap.bssid
            binding.rssiData.text = ap.rssi.toString()
        }
    }

    override fun onDestroyView() {
        Log.v(TAG, "onDestroyView()")
        super.onDestroyView()
        _binding = null
    }
}