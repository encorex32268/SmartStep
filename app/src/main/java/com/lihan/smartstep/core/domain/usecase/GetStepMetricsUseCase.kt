package com.lihan.smartstep.core.domain.usecase

import com.lihan.smartstep.core.data.model.Gender
import com.lihan.smartstep.core.domain.AppSensorManager
import com.lihan.smartstep.core.domain.UnitCalculator
import com.lihan.smartstep.core.domain.UserDataStore
import com.lihan.smartstep.core.domain.model.StepMetrics
import com.lihan.smartstep.core.domain.util.TimerFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetStepMetricsUseCase(
    private val appSensorManager: AppSensorManager,
    private val userDataStore: UserDataStore
) {
    operator fun invoke(): Flow<StepMetrics>{
        return combine(
            TimerFlow.timeAndEmit(),
            appSensorManager.stepsFlow,
            userDataStore.todaySteps,
            userDataStore.userData,
            userDataStore.stepGoal
        ){ time , steps, todaySteps,userData , stepGoal ->

            val currentSteps = if (steps == 0) todaySteps else steps
            val stepGoal = stepGoal
            val height = userData?.height?.toIntOrNull() ?: 175
            val weight = userData?.weight?.toIntOrNull() ?: 60
            val gender = userData?.gender?: Gender.Male.name

            val kcal = UnitCalculator.calculateKcal(
                steps = currentSteps,
                weightKg = weight,
                genderName = gender
            )

            val progress = if (stepGoal > 0) (currentSteps.toFloat() / stepGoal).coerceAtMost(1.0f) else 0f

            val distance = UnitCalculator.calculateDistance(
                heightCm = height,
                steps = currentSteps
            )

            StepMetrics(
                time = time,
                steps = currentSteps,
                kcal = kcal,
                stepGoal = stepGoal,
                progress = progress,
                distance = distance
            )

        }
    }
}