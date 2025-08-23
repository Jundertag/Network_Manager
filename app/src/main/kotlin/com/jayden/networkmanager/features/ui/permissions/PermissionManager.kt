package com.jayden.networkmanager.features.ui.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class PermissionManager(
    activity: ComponentActivity,
    context: Context,
    private val permissionResult: (Map<String, Boolean>) -> Unit
) {

    private val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {


        permissionResult.invoke(it)

    }

    fun isGranted(context: Context, permissions: Array<String>): Map<String, Boolean> {
        return permissions.associateWith { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }


    fun requestPermissions(permissions: Array<String>) {
        launcher.launch(permissions)
    }
}