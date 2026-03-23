package com.lihan.smartstep.coach.domain

interface AIGenerator {
    suspend fun sendSuggestion(text: String): String?
}