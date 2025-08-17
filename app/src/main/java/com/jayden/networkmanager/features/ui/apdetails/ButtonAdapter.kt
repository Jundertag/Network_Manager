package com.jayden.networkmanager.features.ui.apdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jayden.networkmanager.databinding.RowButtonBinding
import com.jayden.networkmanager.features.domain.button.Button

class ButtonAdapter(
    private val onClick: (Button) -> Unit
) : ListAdapter<Button, ButtonAdapter.ButtonViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Button>() {
        override fun areItemsTheSame(oldItem: Button, newItem: Button): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Button, newItem: Button): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowButtonBinding.inflate(inflater, parent, false)
        return ButtonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ButtonViewHolder(
        private val binding: RowButtonBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(button: Button) {
            binding.button.text = button.title
            binding.button.setOnClickListener {
                onClick(button)
            }
        }
    }
}