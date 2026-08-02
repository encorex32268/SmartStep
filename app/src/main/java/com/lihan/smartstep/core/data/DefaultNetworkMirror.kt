package com.lihan.smartstep.core.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.lihan.smartstep.core.domain.NetworkMirror
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class DefaultNetworkMirror(
    private val context: Context
): NetworkMirror{
    override val isConnecting: Flow<Boolean>
        get() = callbackFlow {

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (connectivityManager == null){
                trySend(false)
                close()
                return@callbackFlow
            }


            val currentActiveNetwork = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(currentActiveNetwork)
            val isInitiallyConnected = capabilities?.let {
                it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } ?: false
            trySend(isInitiallyConnected)

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            val networkCallback = object: ConnectivityManager.NetworkCallback(){
                private val validNetworks = mutableSetOf<Network>()

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    validNetworks.add(network)
                    trySend(validNetworks.isNotEmpty())
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    validNetworks.remove(network)
                    trySend(validNetworks.isNotEmpty())
                }
            }
            connectivityManager.registerNetworkCallback(
                request,
                networkCallback
            )

            awaitClose {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }.distinctUntilChanged()
}