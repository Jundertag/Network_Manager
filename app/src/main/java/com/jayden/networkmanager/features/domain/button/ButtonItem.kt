package com.jayden.networkmanager.features.domain.button

data class ButtonItem(
    val iconRes: Int,
    val text: String,
    val onClick: () -> Unit
)