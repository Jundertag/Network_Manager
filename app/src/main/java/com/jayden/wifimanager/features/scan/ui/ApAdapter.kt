package com.jayden.wifimanager.features.scan.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jayden.wifimanager.databinding.RowApBinding
import com.jayden.wifimanager.model.AccessPoint

class ApAdapter(
    private val onClick: (AccessPoint) -> Unit
) : ListAdapter<AccessPoint, ApAdapter.ApViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<AccessPoint>() {
        override fun areItemsTheSame(a: AccessPoint, b: AccessPoint) = a.bssid == b.bssid
        override fun areContentsTheSame(a: AccessPoint, b: AccessPoint) = a == b
    }

    inner class ApViewHolder(private val binding: RowApBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(accessPoint: AccessPoint) {
            binding.ssidText.text = accessPoint.ssid
            binding.bssidText.text = accessPoint.bssid
            itemView.setOnClickListener { onClick(accessPoint) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ApViewHolder(RowApBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ApViewHolder, position: Int) =
        holder.bind(getItem(position)) // <-- use getItem()
}