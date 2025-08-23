package com.jayden.networkmanager.features.presentation.dialogalert

import androidx.lifecycle.ViewModel
import com.jayden.networkmanager.features.domain.dialogalert.DialogAlert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DialogAlertViewModel : ViewModel() {

    private val _items = MutableStateFlow(DialogAlert("","",""))
    val items = _items.asStateFlow()

    companion object {
        private const val TAG = "DialogAlertViewModel"
    }

    fun showAlert(key: String) {
        when (key) {
            DialogKey.KEY_WIFI_THROTTLE -> showWifiThrottleAlert()
        }
    }

    private fun showWifiThrottleAlert() {
        _items.value = DialogAlert(
            "Scans are throttled",
            "By default, the Access Point (AP) list will update every time the OS scans for new APs. The OS will scan every 20 to 160+ seconds depending on the quality of the connection. If you're disconnected from a network, the OS scans for APs periodically to find a network to connect to",
            "Swipe down to Scan Manually. Be warned, this feature is deprecated as of Android 9 and above."
        )
    }
}