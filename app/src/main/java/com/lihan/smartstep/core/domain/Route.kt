package com.lihan.smartstep.core.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data class ProfileSetup(
        val isFromDashboard: Boolean
    ): Route

    @Serializable
    data object Dashboard: Route

}