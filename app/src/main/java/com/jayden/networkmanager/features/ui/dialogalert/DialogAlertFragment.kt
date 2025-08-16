package com.jayden.networkmanager.features.ui.dialogalert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jayden.networkmanager.databinding.DialogAlertBinding
import com.jayden.networkmanager.features.presentation.dialogalert.DialogAlertViewModel
import com.jayden.networkmanager.features.presentation.dialogalert.DialogKey
import kotlinx.coroutines.launch

class DialogAlertFragment : Fragment() {

    private var _binding: DialogAlertBinding? = null
    val viewModel = DialogAlertViewModel()

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect {
                binding.alertTitle.text = it.title
                binding.alertDetails.text = it.description
                binding.alertTipDetails.text = it.tip
            }
        }

        viewModel.showAlert(DialogKey.KEY_WIFI_THROTTLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}