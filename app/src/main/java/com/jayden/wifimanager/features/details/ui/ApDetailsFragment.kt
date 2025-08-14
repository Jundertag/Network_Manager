package com.jayden.wifimanager.features.details.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jayden.wifimanager.databinding.FragmentApDetailsBinding
import com.jayden.wifimanager.features.details.presentation.ApDetailsViewModel
import com.jayden.wifimanager.features.main.presentation.ApViewModel
import com.jayden.wifimanager.features.models.AccessPoint
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
        _binding = FragmentApDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                apViewModel.selected.collect { ap ->
                    ap?.let { render(it) }
                }
            }
        }
        startPostponedEnterTransition()
    }

    private fun render(ap: AccessPoint?) {
        Log.d(TAG, "$ap")
        if (ap != null)
            binding.title.text = ap.ssid

    }
}