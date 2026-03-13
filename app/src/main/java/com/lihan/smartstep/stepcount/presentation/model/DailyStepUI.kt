package com.lihan.smartstep.stepcount.presentation.model

data class DailyStepUI(
    val steps: String,
    val date: String,
    val time: Long,
    val goalSteps: String
)

/**
 *  1. goalStep: 6,000 , steps: 2,300 , date: 2026-03-05
 *
 */