package com.example.clonepexel.broadcast_reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectivityReceiver(private val listener: ConnectivityListener) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val isConnected = cm.activeNetworkInfo?.isConnectedOrConnecting == true
            listener.onNetworkConnectionChanged(isConnected)
        }
    }

    interface ConnectivityListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}