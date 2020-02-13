package com.test.punkapi.ui.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.test.punkapi.App

class NetworkChecker {
    companion object {
        fun isConnected(): Boolean {
            val conman = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT < 23){
                val netinfo = conman.activeNetworkInfo
                if (netinfo != null)
                    netinfo.isConnected && (netinfo.type == ConnectivityManager.TYPE_WIFI || netinfo.type == ConnectivityManager.TYPE_MOBILE)
                else
                    false
            } else {
                val network = conman.activeNetwork
                if (network != null){
                    val netcap = conman.getNetworkCapabilities(network)!!
                    (netcap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || netcap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                } else
                    false
            }
        }
    }
}