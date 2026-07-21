package com.lihan.smartstep.core.presentation.components

import com.lihan.smartstep.core.data.model.HeightUnit
import com.lihan.smartstep.core.data.model.HeightUnit.Companion.formattedName
import com.lihan.smartstep.core.data.model.WeightUnit
import com.lihan.smartstep.profile_setup.presentation.model.feetInchesToCm
import com.lihan.smartstep.profile_setup.presentation.model.toFeetAndInches
import kotlin.math.roundToInt

data class WheelPickerData(
    val value: String,
    val items: List<String>,
    val unit: String = "",
){
    companion object{
        val FtRange = IntRange(start = 3 , endInclusive = 8).toList().map { it.toString() }
        val InchRange = IntRange(start = 0 , endInclusive = 12).toList().map { it.toString() }
        val CmRange = IntRange(start = 120 , endInclusive = 230).toList().map { it.toString() }
        val KgRange = IntRange(start = 20, endInclusive = 300).toList().map { it.toString() }
        val LbsRange = IntRange(start = 44, endInclusive = 660).toList().map { it.toString() }


        fun buildWeightWheelPickerDataListByUnit(
            unit: WeightUnit,
            currentWeight: String,
            currentWeightItems: List<WheelPickerData>,
        ): List<WheelPickerData> {
            val isKgWheel = currentWeightItems.isKgWheelPicker
            val currentValue = currentWeightItems.getOrNull(0)?.value ?: currentWeight
            return when(unit){
                WeightUnit.Kg -> {
                    val value = if (isKgWheel){
                        currentValue
                    }else{
                        val lbsVal = currentValue.toDoubleOrNull() ?: 143.0
                        WeightUnit.Lbs.toKg(lbsVal).roundToInt().toString()
                    }
                    buildWeightKgWheelPickerDataList(value)
                }
                WeightUnit.Lbs -> {
                    val value = if (isKgWheel){
                        val kgVal = currentValue.toDoubleOrNull() ?: 65.0
                        WeightUnit.Lbs.fromKg(kgVal).roundToInt().toString()
                    }else{
                        currentValue
                    }
                    buildWeightLbsWheelPickerDataList(value)
                }
            }

        }

        private fun buildWeightKgWheelPickerDataList(value: String = "65"): List<WheelPickerData>{
            return listOf(
                WheelPickerData(
                    value = value,
                    items = KgRange
                )
            )
        }
        private fun buildWeightLbsWheelPickerDataList(value: String = "143"): List<WheelPickerData>{
            return listOf(
                WheelPickerData(
                    value = value,
                    items = LbsRange
                )
            )
        }

        fun buildHeightWheelPickerDataListByUnit(
            currentHeightValue: String,
            currentHeightItems: List<WheelPickerData>,
            unit: HeightUnit
        ): List<WheelPickerData>{
            return when(unit){
                HeightUnit.Cm-> {
                    val isSingleWheelPicker = currentHeightItems.isSingleWheelPicker
                    val value = if (!isSingleWheelPicker){
                        val feet = currentHeightItems.getOrNull(0)?.value
                        val inch = currentHeightItems.getOrNull(1)?.value
                        "$feet'$inch\"".feetInchesToCm()?:currentHeightValue
                    }else{
                        currentHeightItems.getOrNull(0)?.value ?: currentHeightValue
                    }
                    buildHeightCmWheelPickerDataList(value)
                }
                HeightUnit.FtIn -> {
                    val isSingleWheelPicker = currentHeightItems.isSingleWheelPicker
                    if (isSingleWheelPicker) {
                        val cmStr = currentHeightItems.getOrNull(0)?.value ?: currentHeightValue
                        val feetInches = cmStr.toIntOrNull()?.toFeetAndInches()?.toString() ?: "5'7\""
                        val formatted = feetInches.replace("\"", "").split("'")
                        val feet = formatted.first()
                        val inch = formatted.last()
                        buildHeightFtInWheelPickerDataList(feet, inch)
                    } else {
                        val feet = currentHeightItems.getOrNull(0)?.value ?: "5"
                        val inch = currentHeightItems.getOrNull(1)?.value ?: "7"
                        buildHeightFtInWheelPickerDataList(feet, inch)
                    }
                }
            }
        }

        private fun buildHeightCmWheelPickerDataList(value: String = "175"): List<WheelPickerData>{
            return listOf(
                WheelPickerData(
                    value = value,
                    items = CmRange
                )
            )
        }

        private fun buildHeightFtInWheelPickerDataList(feet: String, inch: String): List<WheelPickerData>{
            return listOf(
                WheelPickerData(
                    value = feet,
                    items = FtRange,
                    unit =  HeightUnit.FtIn.formattedName.split("/").getOrNull(0)?:"ft"
                ),
                WheelPickerData(
                    value = inch,
                    items = InchRange,
                    unit = HeightUnit.FtIn.formattedName.split("/").getOrNull(1)?:"in"
                ),
            )
        }
    }

}

val List<WheelPickerData>.isSingleWheelPicker: Boolean
    get() = if (this.isEmpty()) true else {
        this.first().items.last() == WheelPickerData.CmRange.last()
    }

val List<WheelPickerData>.isKgWheelPicker: Boolean
    get() = if (this.isEmpty()) true else{
        this.first().items.last() == WheelPickerData.KgRange.last()
    }

