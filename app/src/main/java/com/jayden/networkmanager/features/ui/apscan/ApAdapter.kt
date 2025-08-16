package com.jayden.networkmanager.features.ui.apscan

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jayden.networkmanager.databinding.RowApBinding
import com.jayden.networkmanager.features.datamodels.wifi.AccessPoint

class ApAdapter(
    private val onClick: (AccessPoint) -> Unit
) : ListAdapter<AccessPoint, ApAdapter.ApViewHolder>(DiffCallback) {
    companion object {
        private const val TAG = "ApAdapter"
    }

    object DiffCallback : DiffUtil.ItemCallback<AccessPoint>() {
        override fun areItemsTheSame(a: AccessPoint, b: AccessPoint): Boolean {
            return a.bssid == b.bssid
        }

        override fun areContentsTheSame(a: AccessPoint, b: AccessPoint): Boolean {
            return a == b
        }

        override fun getChangePayload(oldItem: AccessPoint, newItem: AccessPoint): Any? {
            return if (oldItem.rssi != newItem.rssi &&
                oldItem.ssid == newItem.ssid &&
                oldItem.capabilities == newItem.capabilities
            ) {
                newItem.rssi
            } else {
                null
            }
        }
    }

    inner class ApViewHolder(
        private val binding: RowApBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(accessPoint: AccessPoint) {
            binding.ssidText.text = accessPoint.ssid
            binding.bssidText.text = accessPoint.bssid
            binding.rssiText.text = buildString {
                append(accessPoint.rssi)
                append(" dBm")
            }
            binding.capabilitiesText.text = accessPoint.capabilities
            itemView.setOnClickListener {
                Log.d(TAG, "onClick($accessPoint)")
                itemView.isEnabled = false
                onClick(accessPoint)
                itemView.postDelayed({ itemView.isEnabled = true }, 350)
            }
        }

        fun bindRssi(rssi: Int) {
            binding.rssiText.text = buildString {
                append(rssi)
                append(" dBm")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowApBinding.inflate(inflater, parent, false)
        return ApViewHolder(binding)
    }

    override fun onBindViewHolder(apViewHolder: ApViewHolder, position: Int) {
        apViewHolder.bind(getItem(position)) // <-- use getItem()
    }

    override fun onBindViewHolder(apViewHolder: ApViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(apViewHolder, position)
        }

        val rssi = payloads.lastOrNull() as? Int
        if (rssi != null) {
            apViewHolder.bindRssi(rssi)
        } else {
            onBindViewHolder(apViewHolder, position)
        }
    }
}