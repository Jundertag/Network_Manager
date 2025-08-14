package com.jayden.wifimanager.features.scan.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jayden.wifimanager.databinding.RowApBinding
import com.jayden.wifimanager.features.models.AccessPoint

class ApAdapter(
    private val onClick: (AccessPoint) -> Unit
) : ListAdapter<AccessPoint, ApAdapter.ApViewHolder>(DiffCallback) {
    companion object {
        private const val TAG = "ApAdapter"
    }

    object DiffCallback : DiffUtil.ItemCallback<AccessPoint>() {
        override fun areItemsTheSame(a: AccessPoint, b: AccessPoint) = a.bssid == b.bssid
        override fun areContentsTheSame(a: AccessPoint, b: AccessPoint) = a == b
    }

    inner class ApViewHolder(
        private val binding: RowApBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(accessPoint: AccessPoint) {
            binding.ssidText.text = accessPoint.ssid
            binding.bssidText.text = accessPoint.bssid
            binding.capabilitiesText.text = accessPoint.capabilities
            itemView.setOnClickListener {
                Log.d(TAG, "onClick($accessPoint)")
                itemView.isEnabled = false
                onClick(accessPoint)
                itemView.postDelayed({ itemView.isEnabled = true }, 350)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ApViewHolder(RowApBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(apViewHolder: ApViewHolder, position: Int) {
        apViewHolder.bind(getItem(position)) // <-- use getItem()
    }
}