package com.lihan.smartstep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.domain.AppDataStore
import com.lihan.smartstep.core.domain.Route
import com.lihan.smartstep.stepcount.data.worker.DailySyncWorkerScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val appDataStore: AppDataStore,
    private val scheduler: DailySyncWorkerScheduler
): ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val isSetProfile = appDataStore.getIsSetting().first()

            //trigger Worker
            scheduler.triggerSync()

            _state.update { it.copy(
                startDestination = if (isSetProfile){
                    Route.SmartStep
                }else{
                    Route.OnboardingProfileSetting
                },
                isReady = true
            ) }
        }

    }
}