package com.jayden.wifimanager.features.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jayden.wifimanager.databinding.FragmentApDetailsBinding

class ApDetailsFragment : Fragment() {

    private var _binding: FragmentApDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}