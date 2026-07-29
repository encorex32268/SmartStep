package com.lihan.smartstep.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val gender: String,
    val height: String,
    val heightUnit: String,
    val weight: String,
    val weightUnit: String
)