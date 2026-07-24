package com.lihan.smartstep.dashboard.data

import android.content.Context
import android.os.PowerManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.lihan.smartstep.dashboard.domain.AppPowerManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class DefaultAppPowerManager(
    private val context: Context
): AppPowerManager {

    private val powerManager by lazy {
        context.getSystemService(Context.POWER_SERVICE) as? PowerManager
    }

    private fun getIgnoringStatus(): Boolean {
        return powerManager?.isIgnoringBatteryOptimizations(context.packageName)?:false
    }

    override val isIgnoringBatteryOptimizationsFlow: Flow<Boolean> = callbackFlow {

        val isIgnoring = getIgnoringStatus()
        trySend(isIgnoring)

        val lifecycleObserver = LifecycleEventObserver { source, event ->
            when(event){
                Lifecycle.Event.ON_RESUME -> {
                    trySend(getIgnoringStatus())
                }
                else -> Unit
            }
        }

        val appLifecycle = ProcessLifecycleOwner.get().lifecycle
        appLifecycle.addObserver(lifecycleObserver)

        awaitClose{
            appLifecycle.removeObserver(lifecycleObserver)
        }
    }.distinctUntilChanged()


}