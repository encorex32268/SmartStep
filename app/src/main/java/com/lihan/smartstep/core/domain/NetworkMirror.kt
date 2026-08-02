package com.lihan.smartstep.core.domain

import kotlinx.coroutines.flow.Flow

interface NetworkMirror {
    val isConnecting: Flow<Boolean>
}