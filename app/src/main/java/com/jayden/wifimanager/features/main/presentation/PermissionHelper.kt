package com.jayden.wifimanager.features.main.presentation

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

data class PermissionsCallback(
    val granted: List<String>,
    val denied: List<String>,
    val permanentlyDenied: List<String>,
    val raw: Map<String, Boolean>
)

class PermissionHelper(
    private val context: Context,
    caller: ActivityResultCaller,
    private val shouldShowRationale: (String) -> Boolean
) {
    private var onResult: ((PermissionsCallback) -> Unit)? = null

    private val launcher: ActivityResultLauncher<Array<String>> =
        caller.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            onResult?.invoke(process(result))
            onResult = null
        }

    fun request(permissions: Array<String>, onResult: (PermissionsCallback) -> Unit) {
        val toRequest = permissions
            .filter { isNotGranted(it) && shouldShowRationale(it) }
            .toTypedArray()

        this.onResult = onResult

        if (toRequest.isEmpty()) {
            onResult(process(emptyMap()))
        } else {
            launcher.launch(toRequest)
        }
    }

    fun granted(permissions: Array<String>) = permissions.filter { isGranted(it) }
    fun denied(permissions: Array<String>) =
        permissions.filter { isNotGranted(it) && shouldShowRationale(it) }
    fun permanentlyDenied(permissions: Array<String>) =
        permissions.filter { isNotGranted(it) && !shouldShowRationale(it) }

    private fun process(result: Map<String, Boolean>): PermissionsCallback {
        // If result is empty, compute from current state (useful when nothing needed requesting)
        val universe = if (result.isEmpty()) emptyList() else result.keys.toList()

        val granted = universe.filter { isGranted(it) }
        val stillDenied = universe.filter { isNotGranted(it) }
        val denied = stillDenied.filter { shouldShowRationale(it) }
        val permanentlyDenied = stillDenied.filter { !shouldShowRationale(it) }

        return PermissionsCallback(granted, denied, permanentlyDenied, result)
    }

    private fun isGranted(p: String) =
        ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED

    private fun isNotGranted(p: String) = !isGranted(p)
}