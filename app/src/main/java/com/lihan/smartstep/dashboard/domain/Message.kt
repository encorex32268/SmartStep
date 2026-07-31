package com.lihan.smartstep.dashboard.domain

import com.lihan.smartstep.dashboard.presentation.aicoach.components.Sender

data class Message(
    val sender: Sender,
    val message: String
)
