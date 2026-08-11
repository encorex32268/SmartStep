package com.lihan.smartstep.dashboard.presentation.report.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.lihan.smartstep.core.presentation.AppIcons

enum class ReportType {
    Steps,Calories,Minutes,Kilometers;
    companion object{
        fun ReportType.toUnit(): String{
            return when(this){
                ReportType.Steps -> "steps"
                ReportType.Calories -> "kcal"
                ReportType.Minutes -> "min"
                ReportType.Kilometers -> "km"
            }
        }

        fun getBottomItemNames(): List<String>{
            return ReportType.entries.map { it.toBottomItemName() }
        }

        fun ReportType.toBottomItemName(): String {
            return when(this){
                ReportType.Steps -> "Steps"
                ReportType.Calories -> "Calories"
                ReportType.Minutes -> "Time"
                ReportType.Kilometers -> "Distance"
            }
        }

        val ReportType.imageVector: ImageVector
            @Composable
            get(){
                return when(this){
                    ReportType.Steps -> AppIcons.Sneakers
                    ReportType.Calories -> AppIcons.Weight
                    ReportType.Minutes -> AppIcons.TimeClock
                    ReportType.Kilometers -> AppIcons.LocationDirection
                }
            }

    }
}