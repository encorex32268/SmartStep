package com.lihan.smartstep.core.domain

interface FileLogger {
    suspend fun writeText(text: String)
}