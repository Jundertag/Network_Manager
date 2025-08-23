package com.jayden.networkmanager.features.domain.button

interface ButtonAction {
    val id: String
    @get:androidx.annotation.StringRes
    val labelRes: Int
    @get:androidx.annotation.DrawableRes
    val iconRes: Int
    val enabled: kotlinx.coroutines.flow.Flow<Boolean> get() = kotlinx.coroutines.flow.flowOf(true)
    val visible: kotlinx.coroutines.flow.Flow<Boolean> get() = kotlinx.coroutines.flow.flowOf(true)
    fun onClick()
}

data class ButtonRow(val actions: List<ButtonAction>)