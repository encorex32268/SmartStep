package com.lihan.smartstep.dashboard.presentation.aicoach


import com.lihan.smartstep.R
import com.lihan.smartstep.dashboard.domain.Message

data class AICoachState(
    val messages: List<Message> = emptyList(),
    val isThinking: Boolean = false,
)

val quickSuggestions: List<Int>
    get() = listOf(
        R.string.recommend_workout,
        R.string.explain_today_trend,
        R.string.how_to_reach_today_goal,
    )