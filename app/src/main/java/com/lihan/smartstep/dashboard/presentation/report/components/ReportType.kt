package com.lihan.smartstep.dashboard.presentation.report.components

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

        private fun ReportType.toBottomItemName(): String {
            return when(this){
                ReportType.Steps -> "Steps"
                ReportType.Calories -> "Calories"
                ReportType.Minutes -> "Time"
                ReportType.Kilometers -> "Distance"
            }
        }
    }
}