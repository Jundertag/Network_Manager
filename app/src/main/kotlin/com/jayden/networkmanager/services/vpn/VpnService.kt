package com.jayden.networkmanager.services.vpn

import android.annotation.SuppressLint
import android.content.Intent
import android.net.VpnService
import android.os.Binder
import android.os.IBinder
import com.jayden.networkmanager.data.api.vpn.VpnApi

@SuppressLint("VpnServicePolicy")
class VpnService : VpnService(), VpnApi {

    private val builder = Builder().apply {
    }

    private val localBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): VpnService = this@VpnService
    }

    override fun onBind(intent: Intent): IBinder? {
        return if (SERVICE_INTERFACE == intent.action) {
            super.onBind(intent)
        } else {
            localBinder
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onRevoke() {
        stop()
        super.onRevoke()
    }

    override fun onDestroy() {
        stop()
        super.onDestroy()
    }

    override fun protect(socket: Int): Boolean {
        return super.protect(socket)
    }


    private fun start() {

    }

    private fun stop() {

    }
}