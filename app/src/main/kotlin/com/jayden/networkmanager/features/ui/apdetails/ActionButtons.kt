package com.jayden.networkmanager.features.ui.apdetails

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jayden.networkmanager.R
import com.jayden.networkmanager.features.domain.button.ButtonRow
import kotlinx.coroutines.launch

typealias ActionId = String
class ActionButtons() {

    fun LinearLayout.bindRow(
        row: ButtonRow,
        lifecycleOwner: androidx.lifecycle.LifecycleOwner
    ) {
        orientation = LinearLayout.HORIZONTAL
        removeAllViews()

        val inflater = LayoutInflater.from(context)
        row.actions.forEach { action ->
            val button = inflater.inflate(R.layout.action_button, this, false) as com.google.android.material.button.MaterialButton
            button.setIconResource(action.iconRes)
            button.setText(action.labelRes)
            button.setOnClickListener { action.onClick() }

            lifecycleOwner.lifecycleScope.launch {
                lifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                    launch {
                        action.enabled.collect { enabled ->
                            button.isEnabled = enabled
                        }
                    }

                    launch {
                        action.visible.collect { visible ->
                            button.isVisible = visible; button.isGone = !visible
                        }
                    }
                }
            }

            addView(button)
        }
    }

}