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
import com.jayden.networkmanager.features.ui.apdetails.ActionButtons
import kotlinx.coroutines.launch

class ApDetailsFragment : Fragment() {

    companion object {
        private const val TAG = "ApDetailsFragment"
    }

    private val apViewModel: ApViewModel by activityViewModels()

    private val apDetailsViewModel: ApDetailsViewModel by viewModels()

    private val buttons = ActionButtons().buttons

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

        setupButtons()
    }

    private fun setupButtons() {
        val rowHPadding = resources.getDimensionPixelSize(R.dimen.row_h_padding)
        val rowVPadding = resources.getDimensionPixelSize(R.dimen.row_v_padding)
        val iconSize = resources.getDimensionPixelSize(R.dimen.row_icon_size)
        val iconMargin = resources.getDimensionPixelSize(R.dimen.row_icon_margin)

        buttons.forEach { button ->

            val container = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also { layoutParams ->
                    layoutParams.setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen.row_spacing))
                }
                setPadding(rowHPadding, rowVPadding, rowHPadding, rowVPadding)
                isClickable = true
                isFocusable = true
                setBackgroundResource(android.R.drawable.list_selector_background)
            }

            val icon = ImageView(requireContext()).apply {
                setImageResource(button.iconRes)
                layoutParams = LinearLayout.LayoutParams(iconSize, iconSize).apply {
                    marginEnd = iconMargin
                    gravity = Gravity.CENTER_VERTICAL
                }
                contentDescription = null
            }

            val label = TextView(requireContext()).apply {
                text = button.text
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply { gravity = Gravity.CENTER_VERTICAL }
            }

            container.setOnClickListener { button.onClick() }

            container.addView(icon)
            container.addView(label)

            binding.buttonListLayout.addView(container)
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